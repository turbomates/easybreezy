package io.easybreezy.hr.model.event

import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableClass
import io.easybreezy.infrastructure.exposed.dao.EmbeddableTable

class Conditions private constructor() : Embeddable() {
    var isPrivate by ConditionsTable.isPrivate
        private set
    var isFreeEntry by ConditionsTable.isFreeEntry
        private set
    var isRepeatable by ConditionsTable.isRepeatable
        private set

    companion object : EmbeddableClass<Conditions>(Conditions::class) {
        fun create(isPrivate: Boolean, isFreeEntry: Boolean, isRepeatable: Boolean): Conditions {
            val conditions = Conditions()
            conditions.isPrivate = isPrivate
            conditions.isFreeEntry = isFreeEntry
            conditions.isRepeatable = isRepeatable
            return conditions
        }

        override fun createInstance(): Conditions {
            return Conditions()
        }
    }

    fun makePrivate() {
        require(!isPrivate) { "Already private" }
        isPrivate = true
    }

    fun makePublic() {
        require(isPrivate) { "Already public" }
        isPrivate = false
    }

    fun closeEntry() {
        require(isFreeEntry) { "Already closed entry" }
        isFreeEntry = false
    }

    fun openEntry() {
        require(!isFreeEntry) { "Already opened entry" }
        isFreeEntry = true
    }

    fun makeRepeatable() {
        require(!isRepeatable) { "Already repeatable" }
        isRepeatable = true
    }

    fun makeDoneOnce() {
        require(isRepeatable) { "Already not repeatable" }
        isRepeatable = false
    }
}

object ConditionsTable : EmbeddableTable() {
    val isPrivate = bool("is_private")
    val isFreeEntry = bool("is_free_entry")
    val isRepeatable = bool("is_repeatable")
}
