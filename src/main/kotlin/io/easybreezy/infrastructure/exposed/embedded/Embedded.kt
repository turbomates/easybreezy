package io.easybreezy.infrastructure.exposed.embedded

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.exceptions.DuplicateColumnException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import java.math.BigDecimal
import java.util.LinkedHashMap
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSubclassOf

inline fun <reified T : Embeddable> Table.embedded(obj: EmbeddableTable, prefix: String? = null): EmbeddableColumn<T> {
    return obj.getColumn(this, prefix)
}

abstract class EmbeddableClass<T : Embeddable>(private val type: KClass<T>) {
    open fun createInstance(): T {
        return type.createInstance()
    }
}

open class Embeddable {
    internal var readValues = LinkedHashMap<String, Any?>()
    internal var writeValues = LinkedHashMap<String, Any>()
    internal var entity: Entity? = null

    operator fun <T : Any> ColumnType<T>.getValue(embeddable: Embeddable, property: KProperty<*>): T {
        if (writeValues.containsKey(this.name)) {
            return writeValues[this.name] as T
        }
        return readValues[this.name] as T
    }

    operator fun <T : Any> ColumnType<T>.setValue(embeddable: Embeddable, property: KProperty<*>, value: T) {
        writeValues[this.name] = value
        entity?.let {
            it.applyValue(this.name, value)
        }
    }

    operator fun <T : Embeddable> EmbeddedColumnType<T>.getValue(embeddable: Embeddable, property: KProperty<*>): T {
        val inner = this.createInstance()
        inner.readValues = embeddable.readValues
        inner.writeValues = embeddable.writeValues
        return inner
    }

    operator fun <T : Embeddable> EmbeddedColumnType<T>.setValue(
        embeddable: Embeddable,
        property: KProperty<*>,
        value: T
    ) {
        value.writeValues.forEach {
            embeddable.entity?.let { entity ->
                entity.applyValue(it.key, it.value)
            }
            embeddable.writeValues[it.key] = it.value
        }
    }

    internal data class Entity(
        private val columns: Map<String, Column<Any?>>,
        private val writeValues: LinkedHashMap<Column<Any?>, Any?>
    ) {
        fun <T : Any> applyValue(name: String, value: T) {
            columns[name]?.let { writeValues[it] = value }
        }
    }

}

class EmbeddableColumn<T : Embeddable>(
    internal val columnType: KClass<T>,
    internal var columns: Map<String, Column<Any?>> = emptyMap(),
    internal val embeddableColumns: List<EmbeddableColumn<*>> = emptyList()
) {
    fun createInstance(): T {
        val companion = columnType.companionObjectInstance as? EmbeddableClass<T>
        return companion?.let { it.createInstance() } ?: columnType.createInstance()
    }
}

class ColumnType<T : Any>(val name: String, val columnType: IColumnType, val type: KClass<T>)
class EmbeddedColumnType<T : Embeddable>(val table: EmbeddableTable, val type: KClass<T>, val prefix: String?) {
    fun createInstance(): T {
        return type.createInstance()
    }
}

open class Entity<ID : Comparable<ID>>(id: EntityID<ID>) : Entity<ID>(id) {
    operator fun <T : Embeddable> EmbeddableColumn<T>.getValue(entity: Entity<ID>, desc: KProperty<*>): T {
        val instance = this.createInstance()
        instance.entity =
            Embeddable.Entity(
                this.embeddableColumns.fold(this.columns) { columns, item -> columns + item.columns },
                entity.writeValues
            )
        columns.forEach {
            instance.readValues[it.key] = entity.readValues[it.value]
        }
        embeddableColumns.forEach {
            it.columns.forEach { column ->
                if (entity.readValues.hasValue(column.value)) {
                    instance.readValues[column.key] = entity.readValues[column.value]
                }
            }
        }
        return instance
    }

    operator fun <T : Embeddable> EmbeddableColumn<T>.setValue(entity: Entity<ID>, property: KProperty<*>, value: T) {
        columns.forEach {
            if (value.writeValues.containsKey(it.key)) {
                writeValues[it.value] = value.writeValues[it.key]
            }
        }
        embeddableColumns.forEach {
            it.columns.forEach { column ->
                if (value.writeValues.containsKey(column.key)) {
                    writeValues[column.value] = value.writeValues[column.key]
                }
            }
        }
    }
}

open class EmbeddableTable {
    val columns = mutableListOf<ColumnType<out Any>>()
    val embeddable = mutableListOf<EmbeddedColumnType<out Embeddable>>()

    fun short(name: String): ColumnType<String> = registerColumn(name, ShortColumnType())

    fun integer(name: String): ColumnType<Int> = registerColumn(name, IntegerColumnType())

    fun char(name: String): ColumnType<String> =
        registerColumn(name, CharacterColumnType())

    fun decimal(name: String, precision: Int, scale: Int): ColumnType<BigDecimal> =
        registerColumn(name, DecimalColumnType(precision, scale))

    fun float(name: String): ColumnType<Float> = registerColumn(name, FloatColumnType())

    fun double(name: String): ColumnType<Double> = registerColumn(name, DoubleColumnType())

    fun long(name: String): ColumnType<Long> = registerColumn(name, LongColumnType())

    fun bool(name: String): ColumnType<Boolean> =
        registerColumn(name, BooleanColumnType())

    fun blob(name: String): ColumnType<ExposedBlob> = registerColumn(name, BlobColumnType())

    fun text(name: String, collate: String? = null): ColumnType<String> =
        registerColumn(name, TextColumnType(collate))

    fun binary(name: String, length: Int): ColumnType<ByteArray> =
        registerColumn(name, BinaryColumnType(length))

    fun binary(name: String): ColumnType<ByteArray> =
        registerColumn(name, BasicBinaryColumnType())

    fun uuid(name: String): ColumnType<UUID> = registerColumn(name, UUIDColumnType())

    fun varchar(name: String, length: Int, collate: String? = null): ColumnType<String> =
        registerColumn(name, VarCharColumnType(length, collate))

    inline fun <reified T : Embeddable> embedded(
        obj: EmbeddableTable,
        prefix: String? = null
    ): EmbeddedColumnType<T> {
        return EmbeddedColumnType(obj, T::class, prefix).apply { embeddable.add(this) }
    }

    private inline fun <reified T : Any> registerColumn(name: String, type: IColumnType): ColumnType<T> =
        ColumnType(name, type, T::class).apply { columns.add(this) }

    inline fun <reified T : Embeddable> getColumn(table: Table, prefix: String? = null): EmbeddableColumn<T> {
        val test = prefix
        return EmbeddableColumn(T::class, columns.associateBy({ it.name }) {
            it.registerInTable(table, prefix) as Column<Any?>
        }, embeddable.map { embedded ->
            EmbeddableColumn(embedded.type, embedded.table.columns.associateBy({ it.name }) {
                var fullPrefix =
                    prefix?.let { first -> embedded.prefix?.let { second -> first + "_" + second } ?: first }
                        ?: embedded.prefix
                it.registerInTable(
                    table,
                    fullPrefix
                ) as Column<Any?>
            })
        })
    }

    fun <T : Any> ColumnType<T>.registerInTable(table: Table, prefix: String? = null): Column<T> {
        return table.registerColumn(prefix?.let { prefix + "_" + name } ?: name, columnType)
    }
}