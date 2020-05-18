package io.easybreezy.project.model.issue

import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableClass
import io.easybreezy.infrastructure.exposed.dao.EmbeddableTable
import io.easybreezy.infrastructure.postgresql.PGEnum
import io.ktor.features.NotFoundException

class Priority private constructor() : Embeddable() {
    var value by PriorityTable.value
    var color by PriorityTable.color

    companion object : EmbeddableClass<Priority>(Priority::class) {
        override fun createInstance(): Priority {
            return Priority()
        }

        fun neutral(): Priority {
            val priority = Priority()
            priority.value = 0
            priority.color = Color.WHITE
            return priority
        }

        fun normal(): Priority {
            val priority = Priority()
            priority.value = 0
            priority.color = Color.GREEN
            return priority
        }

        fun low(): Priority {
            val priority = Priority()
            priority.value = -1
            priority.color = Color.YELLOW
            return priority
        }

        fun high(): Priority {
            val priority = Priority()
            priority.value = 1
            priority.color = Color.RED
            return priority
        }

        fun highest(lastHighest: Int): Priority {
            val priority = Priority()
            priority.value = lastHighest + 1
            priority.color = Color.RED
            return priority
        }

        fun lowest(lastLowest: Int): Priority {
            val priority = Priority()
            priority.value = lastLowest - 1
            priority.color = Color.YELLOW
            return priority
        }
    }

    enum class Color(val rgb: String) {
        RED("#FF0000"),
        GREEN("#00FF00"),
        YELLOW("#FFFF00"),
        WHITE("#FFFFFF");

        companion object {
            fun byRGB(rgb: String): Color {
                values().forEach {
                    if (rgb == it.rgb) {
                        return it
                    }
                }
                throw NotFoundException("Color $rgb not found")
            }
        }
    }
}

object PriorityTable : EmbeddableTable() {
    val value = integer("priority")
    val color = customEnumeration(
        "color",
        "priority_color",
        { value -> Priority.Color.byRGB(value as String) },
        { PGEnum("priority_color", it) })
}
