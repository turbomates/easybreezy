package io.easybreezy.project.model.team

import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.ResultRow
import java.util.*

class Team private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var name by Teams.name
    private val repositories by Repository referrersOn Teams.repositories
    private val members by Member referrersOn Teams.members

    companion object : PrivateEntityClass<UUID, Team>(object : Repository() {}) {
        fun create(name: String): Team {
            return Team.new {
                this.name = name
            }
        }

    }

    fun addMember(member: UUID, info: Member.Info, roles: List<Role> = emptyList()) {
    }

    fun addMemberRole(member: UUID, role: Role) {

    }

    fun removeMember(member: UUID) {

    }

    fun addRepository(url: String, type: io.easybreezy.project.model.team.Repository.Type) {

    }


    abstract class Repository : EntityClass<UUID, Team>(Teams, Team::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Team {
            return Team(entityId)
        }

    }
}

class Repository : UUIDEntity(EntityID(UUID.randomUUID(), Repositories)) {
    companion object : PrivateEntityClass<UUID, Repository>(object : UUIDEntityClass<Repository>(Repositories) {})
    enum class Type {
        GITLAB,
        GITHUB
    }
}

object Teams : UUIDTable() {
    val name = varchar("name", 25)
    val repositories = reference("repositories", Repositories)
    val members = reference("members", Members)
}

object Repositories : UUIDTable() {

}