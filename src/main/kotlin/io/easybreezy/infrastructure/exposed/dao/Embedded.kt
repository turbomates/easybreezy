package io.easybreezy.infrastructure.exposed.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import java.util.LinkedHashMap
import kotlin.reflect.KProperty
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.jvm.jvmErasure

abstract class Embeddable {
    internal val writeValues = LinkedHashMap<Column<Any?>, Any?>()
    internal lateinit var readValues: ResultRow

    operator fun <T> Column<T>.getValue(embeddable: Embeddable, property: KProperty<*>): T {
        return when {
            writeValues.containsKey(this as Column<out Any?>) -> writeValues[this as Column<out Any?>] as T
            columnType.nullable -> readValues[this]
            else -> readValues[this]
        }
    }

    operator fun <T> Column<T>.setValue(embeddable: Embeddable, property: KProperty<*>, value: Any?) {
        val currentValue = readValues?.getOrNull(this)
        if (writeValues.containsKey(this as Column<out Any?>) || currentValue != value) {
            writeValues[this as Column<Any?>] = value
        }
    }

    abstract class EmbeddableClass<T : Embeddable>(private val table: Table) {
        abstract fun createInstance(): T
        internal fun createFromResult(resultRow: ResultRow): T {
            val instance = this.createInstance()
            instance.readValues = resultRow
            return instance
        }

        fun new(init: T.() -> Unit): T {
            val instance = createInstance()
            instance.readValues = ResultRow.createAndFillDefaults(table.columns)
            instance.init()
            return instance
        }
    }
}

abstract class EmbeddableColumn<T : Embeddable> {
    operator fun <TC : Comparable<TC>> getValue(embeddable: Entity<TC>, property: KProperty<*>): T {
        val companion = property.returnType.jvmErasure.companionObjectInstance
        if (companion is Embeddable.EmbeddableClass<*>) {
            return companion.createFromResult(embeddable.readValues) as T
        }
        throw NotImplementedError("Please provide companion class with provide EmbeddableClass")
    }


    operator fun <TC : Comparable<TC>> setValue(embeddable: Entity<TC>, property: KProperty<*>, any: T) {
        if (any is Embeddable) {
            with(embeddable) {
                any.writeValues.forEach {
                    it.key.setValue(embeddable, property, it.value)
                }
            }
        }
    }
}
