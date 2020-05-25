package io.easybreezy.project.model.issue

import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

class Label private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    var name by Labels.name
        private set

    companion object : PrivateEntityClass<UUID, Label>(object : Label.Repository() {}) {
        fun new(name: String): Label {
            return Label.new {
                this.name = name
            }
        }
    }

    abstract class Repository : EntityClass<UUID, Label>(Labels, Label::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Label {
            return Label(entityId)
        }
    }
}

object Labels : UUIDTable("issue_labels") {
    val name = varchar("name", 25).uniqueIndex()
}
