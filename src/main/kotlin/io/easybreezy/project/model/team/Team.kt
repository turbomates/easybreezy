package io.easybreezy.project.model.team

import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import org.jetbrains.exposed.dao.*
import java.util.*

class Team(_project: UUID, _name: String) : AggregateRoot<UUID>(EntityID(UUID.randomUUID(), Teams)) {
    private var name by Teams.name
    private val repositories by Repository referrersOn Teams.repositories
    private val members by Member referrersOn Teams.members

    init {
        name = _name
    }

    fun addMember(member: UUID, info: Member.Info, roles: List<Role> = emptyList()) {
    }

    fun addMemberRole(member: UUID, role: Role) {

    }

    fun removeMember(member: UUID) {

    }

    fun addRepository(url: String, type: Repository.Type) {

    }

    class Repository : UUIDEntity(EntityID(UUID.randomUUID(), Repositories)) {
        companion object : PrivateEntityClass<UUID, Repository>(object : UUIDEntityClass<Repository>(Repositories) {})
        enum class Type {
            GITLAB,
            GITHUB
        }
    }

    companion object : PrivateEntityClass<UUID, Team>(object : EntityClass<UUID, Team>(Teams) {})
}

object Teams : UUIDTable() {
    val name = varchar("name", 25)
    val repositories = reference("repositories", Repositories)
    val members = reference("members", Members)
}

object Repositories : UUIDTable() {

}