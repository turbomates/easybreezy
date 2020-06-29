package io.easybreezy.project.model.issue

import io.easybreezy.infrastructure.event.project.issue.Reassigned
import io.easybreezy.infrastructure.event.project.issue.WatchersAdded
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

class Participant private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var issue by Participants.id
    private var assignee by Participants.assignee
    private var updatedAt by Participants.updatedAt
    private var watchers by Participants.watchers

    companion object : PrivateEntityClass<UUID, Participant>(object : Participant.Repository() {}) {
        fun ofIssue(
            issue: UUID,
            assignee: UUID?,
            watchers: List<UUID>?
        ): Participant {
            return Participant.new {
                this.issue = EntityID(issue, Participants)
                this.assignee = assignee
                this.watchers = watchers ?: listOf()
            }
        }
    }

    fun reassign(reassigned: UUID) {
        updatedAt = LocalDateTime.now()
        addEvent(Reassigned(this.id.value, assignee, reassigned, updatedAt))
        assignee = reassigned
    }

    fun addWatchers(list: List<UUID>) {
        val updated = watchers.toMutableList()
        updated.addAll(list)
        watchers = updated
        updatedAt = LocalDateTime.now()
        addEvent(WatchersAdded(this.id.value, watchers, updatedAt))
    }

    abstract class Repository : EntityClass<UUID, Participant>(Participants, Participant::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Participant {
            return Participant(entityId)
        }
    }
}

object Participants : IdTable<UUID>("issue_participants") {
    override val id: Column<EntityID<UUID>> = uuid("issue").entityId()
    val assignee = uuid("assignee").nullable()
    val watchers = jsonb("watchers", UUIDSerializer.list)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}
