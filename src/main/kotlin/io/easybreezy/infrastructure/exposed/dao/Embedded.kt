package io.easybreezy.infrastructure.exposed.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.`java-time`.JavaInstantColumnType
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import java.math.BigDecimal
import java.time.LocalDate
import java.util.LinkedHashMap
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSubclassOf
import org.jetbrains.exposed.sql.`java-time`.JavaLocalDateColumnType
import org.jetbrains.exposed.sql.`java-time`.JavaLocalDateTimeColumnType
import java.time.Instant
import java.time.LocalDateTime

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

    operator fun <T : Any> Column<T>.getValue(embeddable: Embeddable, property: KProperty<*>): T {
        if (writeValues.containsKey(this.name)) {
            return writeValues[this.name] as T
        }
        return readValues[this.name] as T
    }

    operator fun <T : Any> Column<T>.setValue(embeddable: Embeddable, property: KProperty<*>, value: T) {
        writeValues[this.name] = value
        entity?.let {
            it.applyValue(this.name, value)
        }
    }

    operator fun <T : Embeddable> EmbeddedColumn<T>.getValue(embeddable: Embeddable, property: KProperty<*>): T {
        val inner = this.createInstance()
        inner.readValues = embeddable.readValues
        inner.writeValues = embeddable.writeValues
        return inner
    }

    operator fun <T : Embeddable> EmbeddedColumn<T>.setValue(
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
        private val columns: Map<String, org.jetbrains.exposed.sql.Column<Any?>>,
        private val writeValues: LinkedHashMap<org.jetbrains.exposed.sql.Column<Any?>, Any?>
    ) {
        fun <T : Any> applyValue(name: String, value: T) {
            columns[name]?.let { writeValues[it] = value }
        }
    }

}

class EmbeddableColumn<T : Embeddable>(
    internal val columnType: KClass<T>,
    internal var columns: Map<String, org.jetbrains.exposed.sql.Column<Any?>> = emptyMap(),
    internal val embeddableColumns: List<EmbeddableColumn<*>> = emptyList()
) : Expression<T>() {
    fun createInstance(): T {
        val companion = columnType.companionObjectInstance as? EmbeddableClass<T>
        return companion?.let { it.createInstance() } ?: columnType.createInstance()
    }

    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        columns.forEach { (_, column) ->
            column.toQueryBuilder(queryBuilder)
        }
        embeddableColumns.forEach { column ->
            column.toQueryBuilder(queryBuilder)
        }
    }

    operator fun <T : Any> get(embeddedColumn: Column<T>): org.jetbrains.exposed.sql.Column<Any?> {
        return columns.values.first { column -> column.name == embeddedColumn.name }
    }
}

class Column<T : Any>(val name: String, val columnType: IColumnType, val type: KClass<T>) : Expression<T>() {
    internal var nullable: Boolean = false
    internal var defaultValueFn: (() -> T)? = null
    fun nullable(): Column<T> {
        nullable = true
        return this
    }

    fun default(defaultValue: T): Column<T> {
        defaultValueFn = { defaultValue }
        return this
    }

    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

class EmbeddedColumn<T : Embeddable>(val table: EmbeddableTable, val type: KClass<T>, val prefix: String?) {
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
    val columns = mutableListOf<Column<out Any>>()
    val embeddable = mutableListOf<EmbeddedColumn<out Embeddable>>()

    fun short(name: String): Column<String> = registerColumn(name, ShortColumnType())

    fun integer(name: String): Column<Int> = registerColumn(name, IntegerColumnType())

    fun char(name: String): Column<String> =
        registerColumn(name, CharacterColumnType())

    fun decimal(name: String, precision: Int, scale: Int): Column<BigDecimal> =
        registerColumn(name, DecimalColumnType(precision, scale))

    fun float(name: String): Column<Float> = registerColumn(name, FloatColumnType())

    fun double(name: String): Column<Double> = registerColumn(name, DoubleColumnType())

    fun long(name: String): Column<Long> = registerColumn(name, LongColumnType())

    fun bool(name: String): Column<Boolean> =
        registerColumn(name, BooleanColumnType())

    fun blob(name: String): Column<ExposedBlob> = registerColumn(name, BlobColumnType())

    fun text(name: String, collate: String? = null): Column<String> =
        registerColumn(name, TextColumnType(collate))

    fun binary(name: String, length: Int): Column<ByteArray> =
        registerColumn(name, BinaryColumnType(length))

    fun binary(name: String): Column<ByteArray> =
        registerColumn(name, BasicBinaryColumnType())

    fun uuid(name: String): Column<UUID> = registerColumn(name, UUIDColumnType())

    fun varchar(name: String, length: Int, collate: String? = null): Column<String> =
        registerColumn(name, VarCharColumnType(length, collate))

    fun date(name: String): Column<LocalDate> = registerColumn(name, JavaLocalDateColumnType())

    fun datetime(name: String): Column<LocalDateTime> = registerColumn(name, JavaLocalDateTimeColumnType())

    fun timestamp(name: String): Column<Instant> = registerColumn(name, JavaInstantColumnType())

    inline fun <reified T : Embeddable> embedded(
        obj: EmbeddableTable,
        prefix: String? = null
    ): EmbeddedColumn<T> {
        return EmbeddedColumn(obj, T::class, prefix).apply { embeddable.add(this) }
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Enum<T>> customEnumeration(
        name: String,
        sql: String? = null,
        crossinline fromDb: (Any) -> T,
        crossinline toDb: (T) -> Any
    ): Column<T> {
        return registerColumn(name, object : StringColumnType() {
            override fun sqlType(): String = sql ?: error("Column $name should exists in database ")
            override fun valueFromDB(value: Any): T =
                if (value::class.isSubclassOf(Enum::class)) value as T else fromDb(value)

            override fun notNullValueToDB(value: Any): Any = toDb(value as T)
        })
    }

    inline fun <reified T : Any> registerColumn(name: String, type: IColumnType): Column<T> =
        Column(name, type, T::class).apply { columns.add(this) }

    inline fun <reified T : Embeddable> getColumn(table: Table, prefix: String? = null): EmbeddableColumn<T> {
        return EmbeddableColumn(
            T::class,
            columns.associateBy({ it.name }) {
                it.registerInTable(table, prefix) as org.jetbrains.exposed.sql.Column<Any?>
            },
            embeddable.map { embedded ->
                EmbeddableColumn(
                    embedded.type,
                    embedded.table.columns.associateBy({ it.name }) {
                        var fullPrefix =
                            prefix?.let { first -> embedded.prefix?.let { second -> first + "_" + second } ?: first }
                                ?: embedded.prefix
                        it.registerInTable(
                            table,
                            fullPrefix
                        ) as org.jetbrains.exposed.sql.Column<Any?>
                    })
            })
    }

    fun <T : Any> Column<T>.registerInTable(table: Table, prefix: String? = null): org.jetbrains.exposed.sql.Column<T> {
        columnType.nullable = this.nullable
        val column: org.jetbrains.exposed.sql.Column<T> =
            table.registerColumn(prefix?.let { prefix + "_" + name } ?: name, columnType)
        column.defaultValueFun = this.defaultValueFn
        return column
    }
}