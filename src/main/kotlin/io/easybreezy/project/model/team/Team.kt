package io.easybreezy.project.model.team

import io.easybreezy.infrastructure.event.project.team.*
import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.project.model.Project
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime
import java.util.*

class Team private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var name by Teams.name
    //    private val repositories by Repository referrersOn Teams.repositories
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
        status = Status.CLOSED
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
        status = Status.ACTIVE
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
        members.first { it.id.value == member }.changeRole(role)
        addEvent(MemberRoleChanged(id.value, member, role, updatedAt))
    }

    fun removeMember(member: UUID) {
        updatedAt = LocalDateTime.now()
        members.first { it.id.value == member }.delete()
    }

//    fun addRepository(url: String, type: io.easybreezy.project.model.team.RemoteRepository.Type) {
//    }
//
//    fun removeRepository(url: String) {
//    }

    abstract class Repository : EntityClass<UUID, Team>(Teams, Team::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Team {
            return Team(entityId)
        }
    }
}

enum class Status {
    ACTIVE,
    CLOSED
}


object Teams : UUIDTable("project_teams") {
    val name = varchar("name", 25)
    val project = uuid("project")
    val status = enumerationByName("status", 25, Status::class).default(Status.ACTIVE)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}

interface Repository {
    fun isRoleBelongs(toTeam: UUID, role: UUID): Boolean
}

//open class RemoteRepository : UUIDEntity(EntityID(UUID.randomUUID(), RemoteRepositories)) {
//    companion object : PrivateEntityClass<UUID, RemoteRepository>(object : UUIDEntityClass<RemoteRepository>(RemoteRepositories) {})
//    enum class Type {
//        GITLAB,
//        GITHUB
//    }
//}

//object RemoteRepositories : UUIDTable()