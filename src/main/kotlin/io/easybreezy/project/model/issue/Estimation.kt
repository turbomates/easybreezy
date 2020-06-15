package io.easybreezy.project.model.issue

import io.easybreezy.infrastructure.event.project.issue.DueDateChanged
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

class Estimation private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var issue by Estimations.id
    private var updatedAt by Estimations.updatedAt
    private var dueDate by Estimations.dueDate

    companion object : PrivateEntityClass<UUID, Estimation>(object : Estimation.Repository() {}) {
        fun ofIssue(
            issue: UUID,
            dueDate: LocalDateTime
        ): Estimation {
            return Estimation.new {
                this.issue = EntityID(issue, Estimations)
                this.dueDate = dueDate
            }
        }
    }

    fun changeDueDate(updated: LocalDateTime) {
        updatedAt = LocalDateTime.now()
        addEvent(DueDateChanged(this.id.value, dueDate, updated, updatedAt))
        dueDate = updated
    }

    abstract class Repository : EntityClass<UUID, Estimation>(Estimations, Estimation::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Estimation {
            return Estimation(entityId)
        }
    }
}

object Estimations : IdTable<UUID>("issue_estimations") {
    override val id: Column<EntityID<UUID>> = uuid("issue").entityId()
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
    val dueDate = datetime("due_date").nullable()
}
