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

class Workflow private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var issue by Workflows.id
    private var updatedAt by Workflows.updatedAt
    private var status by Workflows.status

    companion object : PrivateEntityClass<UUID, Workflow>(object : Workflow.Repository() {}) {
        fun ofIssue(
            issue: UUID,
            status: UUID
        ): Workflow {
            return Workflow.new {
                this.issue = EntityID(issue, Workflows)
                this.status = status
            }
        }
    }

    fun updateStatus(updated: UUID) {
        updatedAt = LocalDateTime.now()
        addEvent(StatusUpdated(this.id.value, status, updated, updatedAt))
        status = updated
    }

    abstract class Repository : EntityClass<UUID, Workflow>(Workflows, Workflow::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Workflow {
            return Workflow(entityId)
        }
    }
}

object Workflows : IdTable<UUID>("issue_behaviors") {
    override val id: Column<EntityID<UUID>> = uuid("issue").entityId()
    val status = uuid("status").nullable()
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}
