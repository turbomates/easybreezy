package io.easybreezy.infrastructure.exposed.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import java.util.LinkedHashMap
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.createInstance

open class Embeddable {
    internal var readValues: ResultRow? = null
    internal val writeValues = LinkedHashMap<Column<Any?>, Any?>()
    @Suppress("UNCHECKED_CAST")
    operator fun <T> Column<T>.getValue(embeddable: Embeddable, property: KProperty<*>): T {
        val readValues = readValues(this.table)
        return when {
            writeValues.containsKey(this as Column<out Any?>) -> writeValues[this as Column<out Any?>] as T
            columnType.nullable -> readValues[this]
            else -> readValues[this]
        }
    }

    private fun readValues(table: Table): ResultRow {
        if (readValues == null) {
            readValues = ResultRow.createAndFillDefaults(table.columns)
        }
        return readValues!!
    }

    operator fun <T> Column<T>.setValue(embeddable: Embeddable, property: KProperty<*>, value: Any?) {
        val readValues = readValues(this.table)
        val currentValue = readValues?.getOrNull(this)
        if (writeValues.containsKey(this as Column<out Any?>) || currentValue != value) {
            writeValues[this as Column<Any?>] = value
        }
    }

    override fun equals(other: Any?): Boolean {
        return (other as? Embeddable)?.let { embeddable ->
            val notEquals = embeddable.readValues?.fieldIndex?.filter { (t, _) ->
                val otherValue = embeddable.writeValues.getOrDefault(t, embeddable.readValues?.getOrNull(t))
                val thisValue = writeValues.getOrDefault(t, readValues?.getOrNull(t))
                otherValue != thisValue
            }
            notEquals?.let { it.count() == 0 } ?: false
        } ?: false
    }
}

open class EmbeddableClass<T : Embeddable>(private val entityType: KClass<T>) {
    open fun createInstance(resultRow: ResultRow?): T {
        return entityType.createInstance()
    }
}

class Embedded<T : Embeddable>(private val embeddableClass: EmbeddableClass<T>) : ReadWriteProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        val parent = thisRef as? Entity<*>
        val values = parent?.readValues ?: (thisRef as Embeddable)?.readValues
        val embedded = embeddableClass.createInstance(values)
        values?.let {
            embedded.readValues = it
        }
        return embedded
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        var parent = thisRef as? Entity<*>
        parent?.let {
            it.writeValues += value.writeValues
        } ?: (thisRef as Embeddable)?.let {
            it.writeValues += value.writeValues
        }
    }
}
