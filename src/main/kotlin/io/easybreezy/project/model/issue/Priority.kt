package io.easybreezy.project.model.issue

import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableClass
import io.easybreezy.infrastructure.exposed.dao.EmbeddableTable

class Priority private constructor() : Embeddable() {
    var value by PriorityTable.value

    companion object : EmbeddableClass<Priority>(Priority::class) {
        override fun createInstance(): Priority {
            return Priority()
        }

        fun notDetermined(): Priority {
            val priority = Priority()
            priority.value = 0

            return priority
        }

        fun low(): Priority {
            val priority = Priority()
            priority.value = -1

            return priority
        }

        fun high(): Priority {
            val priority = Priority()
            priority.value = 1

            return priority
        }

        fun highest(lastHighest: Int): Priority {
            val priority = Priority()
            priority.value = lastHighest + 1

            return priority
        }

        fun lowest(lastLowest: Int): Priority {
            val priority = Priority()
            priority.value = lastLowest - 1

            return priority
        }
    }
}

object PriorityTable : EmbeddableTable() {
    val value = integer("priority")
}