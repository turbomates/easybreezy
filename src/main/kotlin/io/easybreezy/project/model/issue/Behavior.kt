package io.easybreezy.project.model.issue

import io.easybreezy.infrastructure.event.project.issue.StatusUpdated
import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime
import java.util.UUID

class Behavior private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var issue by Behaviors.id
    private var updatedAt by Behaviors.updatedAt
    private var status by Behaviors.status

    companion object : PrivateEntityClass<UUID, Behavior>(object : Behavior.Repository() {}) {
        fun ofIssue(
            issue: UUID,
            status: UUID
        ): Behavior {
            return Behavior.new {
                this.issue = EntityID(issue, Behaviors)
                this.status = status
            }
        }
    }

    fun updateStatus(updated: UUID) {
        updatedAt = LocalDateTime.now()
        addEvent(StatusUpdated(this.id.value, status, updated, updatedAt))
        status = updated
    }

    abstract class Repository : EntityClass<UUID, Behavior>(Behaviors, Behavior::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Behavior {
            return Behavior(entityId)
        }
    }
}

object Behaviors : IdTable<UUID>("issue_behaviors") {
    override val id: Column<EntityID<UUID>> = uuid("issue").entityId()
    val status = uuid("status").nullable()
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}
