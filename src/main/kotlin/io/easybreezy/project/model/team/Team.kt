package io.easybreezy.project.model.team

import io.easybreezy.infrastructure.event.project.team.MemberAdded
import io.easybreezy.infrastructure.event.project.team.MemberRoleChanged
import io.easybreezy.infrastructure.event.project.team.NewTeamAdded
import io.easybreezy.infrastructure.event.project.team.TeamActivated
import io.easybreezy.infrastructure.event.project.team.TeamClosed
import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.project.model.Projects
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime
import java.util.UUID

class Team private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var name by Teams.name
    private val members by Member referrersOn Members.team
    private var project by Teams.project
    private var status by Teams.status
    private var updatedAt by Teams.updatedAt

    companion object : PrivateEntityClass<UUID, Team>(object : Repository() {}) {
        fun create(name: String, project: UUID): Team {
            return Team.new {
                this.name = name
                this.project = project
                addEvent(
                    NewTeamAdded(
                        this.project,
                        this.id.value,
                        this.name,
                        LocalDateTime.now()
                    )
                )
            }
        }
    }

    fun close() {
        status = Status.Closed
        updatedAt = LocalDateTime.now()
        addEvent(
            TeamClosed(
                project,
                id.value,
                name,
                updatedAt
            )
        )
    }

    fun activate() {
        status = Status.Active
        updatedAt = LocalDateTime.now()
        addEvent(
            TeamActivated(
                project,
                id.value,
                name,
                updatedAt
            )
        )
    }

    fun addMember(member: UUID, role: UUID) {
        updatedAt = LocalDateTime.now()
        Member.create(this, member, role)
        addEvent(MemberAdded(id.value, member, updatedAt))
    }

    fun changeMemberRole(member: UUID, role: UUID) {
        updatedAt = LocalDateTime.now()
        members.first { it.isUser(member) }.changeRole(role)
        addEvent(MemberRoleChanged(id.value, member, role, updatedAt))
    }

    fun removeMember(member: UUID) {
        updatedAt = LocalDateTime.now()
        members.first { it.isUser(member) }.delete()
    }

    abstract class Repository : EntityClass<UUID, Team>(Teams, Team::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Team {
            return Team(entityId)
        }
    }
}

enum class Status {
    Active,
    Closed
}

object Teams : UUIDTable("project_teams") {
    val name = varchar("name", 25)
    val project = uuid("project").references(Projects.id)
    val status = enumerationByName("status", 25, Status::class).default(Status.Active)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}

interface Repository {
    fun isRoleBelongs(toTeam: UUID, role: UUID): Boolean
    fun isNoActualTickets(member: UUID): Boolean
    fun get(id: UUID): Team
}
