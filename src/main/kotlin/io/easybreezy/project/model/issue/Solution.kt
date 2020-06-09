package io.easybreezy.project.model.issue

import io.easybreezy.infrastructure.event.project.issue.DueDateChanged
import io.easybreezy.infrastructure.event.project.issue.Reassigned
import io.easybreezy.infrastructure.event.project.issue.StatusUpdated
import io.easybreezy.infrastructure.event.project.issue.WatchersUpdated
import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.infrastructure.exposed.type.jsonb
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.builtins.list
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime
import java.util.UUID

class Solution private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var issue by Solutions.id
    private var assignee by Solutions.assignee
    private var updatedAt by Solutions.updatedAt
    private var watchers by Solutions.watchers
    private var status by Solutions.status
    private var dueDate by Solutions.dueDate

    companion object : PrivateEntityClass<UUID, Solution>(object : Solution.Repository() {}) {
        fun ofIssue(
            issue: UUID,
            assignee: UUID?,
            status: UUID?,
            watchers: List<UUID>?,
            dueDate: LocalDateTime?
        ): Solution {
            return Solution.new {
                this.issue = EntityID(issue, Solutions)
                this.assignee = assignee
                this.watchers = watchers ?: listOf()
                this.status = status
                this.dueDate = dueDate
            }
        }
    }

    fun changeDueDate(updated: LocalDateTime) {
        updatedAt = LocalDateTime.now()
        addEvent(DueDateChanged(this.id.value, dueDate, updated, updatedAt))
        dueDate = updated
    }

    fun updateStatus(updated: UUID) {
        updatedAt = LocalDateTime.now()
        addEvent(StatusUpdated(this.id.value, status, updated, updatedAt))
        status = updated
    }

    fun reassign(reassigned: UUID) {
        updatedAt = LocalDateTime.now()
        addEvent(Reassigned(this.id.value, assignee, reassigned, updatedAt))
        assignee = reassigned
    }

    fun updateWatchers(list: List<UUID>) {
        val updated = watchers.toMutableList()
        updated.addAll(list)
        watchers = updated
        updatedAt = LocalDateTime.now()
        addEvent(WatchersUpdated(this.id.value, watchers, updatedAt))
    }

    abstract class Repository : EntityClass<UUID, Solution>(Solutions, Solution::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Solution {
            return Solution(entityId)
        }
    }
}

object Solutions : IdTable<UUID>("issue_solutions") {
    override val id: Column<EntityID<UUID>> = uuid("issue").entityId()
    val assignee = uuid("assignee").nullable()
    val watchers = jsonb("watchers", UUIDSerializer.list)
    val status = uuid("status").nullable()
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
    val dueDate = datetime("due_date").nullable()
}
