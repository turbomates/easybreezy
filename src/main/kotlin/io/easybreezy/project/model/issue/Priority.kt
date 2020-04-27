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

        fun create(value: Int = 0): Priority {
            val priority = Priority()
            priority.value = value

            return priority
        }
    }
}

object PriorityTable : EmbeddableTable() {
    val value = integer("priority")
}