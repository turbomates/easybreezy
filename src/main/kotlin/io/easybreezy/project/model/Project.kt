package io.easybreezy.project.model

import io.easybreezy.infrastructure.event.project.project.*
import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.project.model.team.Role
import io.easybreezy.project.model.team.Roles
import io.easybreezy.project.model.team.Teams
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.text.Normalizer
import java.time.LocalDateTime
import java.util.*

class Project private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var name by Projects.name
    private var author by Projects.author
    private var slug by Projects.slug
    private var description by Projects.description
    private var status by Projects.status
    private var updatedAt by Projects.updatedAt
    private val roles by Role referrersOn Roles.project
    private val teams by Team referrersOn Teams.project

    fun writeDescription(description: String) {
        this.description = description
    }

    fun close() {
        this.status = Status.Closed
        this.updatedAt = LocalDateTime.now()
        this.addEvent(Closed(id.value, this.updatedAt))
    }

    fun suspend() {
        this.status = Status.Suspended
        this.updatedAt = LocalDateTime.now()
        this.addEvent(Suspended(id.value, this.updatedAt))
    }

    fun activate() {
        this.status = Status.Active
        this.updatedAt = LocalDateTime.now()
        this.addEvent(Activated(id.value, this.updatedAt))
    }

    fun addRole(name: String, permissions: List<String>) {
        val newRole = Role.new(this, name, permissions)
        this.updatedAt = LocalDateTime.now()
        this.addEvent(RoleAdded(id.value, newRole.id.value, newRole.name, permissions))
    }

    fun changeRole(name: String, permissions: List<String>, newName: String? = null) {
        val role = roles.first { it.name == name }
        newName?.let { role.rename(it) }
        role.changePermissions(permissions)
        this.updatedAt = LocalDateTime.now()
        this.addEvent(RoleChanged(id.value, role.id.value, role.name, permissions))
    }

    fun removeRole(name: String) {
        val role = roles.first { it.name == name }
        if (role.membersCount() > 0) {
            throw Exception("can't remove role with active members")
        }
        role.delete()
        this.updatedAt = LocalDateTime.now()
        this.addEvent(RoleRemoved(id.value, role.id.value, role.name))
    }

    fun createTeam(name: String) {
        val newTeam = Team.new { this.name = name; this.project = this@Project.id }
        this.updatedAt = LocalDateTime.now()
        this.addEvent(NewTeamAdded(id.value, newTeam.id.value, newTeam.name))
    }

    fun closeTeam(name: String) {
        val team = teams.first { it.name == name }
        team.close()
        this.updatedAt = LocalDateTime.now()
        this.addEvent(TeamClosed(id.value, team.id.value, team.name))
    }

    fun activateTeam(name: String) {
        val team = teams.first { it.name == name }
        team.activate()
        this.updatedAt = LocalDateTime.now()
        this.addEvent(TeamActivated(id.value, team.id.value, team.name))
    }

    enum class Status {
        Active,
        Closed,
        Suspended
    }

    companion object : PrivateEntityClass<UUID, Project>(object : Repository() {}) {
        fun new(name: String, description: String): Project {
            return Project.new {
                this.name = name
                this.author = UUID.randomUUID()
                this.status = Status.Active
                this.slug = slugify(name)
                this.description = description
                defaultRoles(this)
                this.addEvent(Created(this.id.value, this.name))
            }
        }

        private fun defaultRoles(project: Project): List<Role> {
            return listOf("Project Manager", "Team Lead", "Developer").map {
                Role.new(project, it, emptyList())
            }
        }

        private fun slugify(name: String): String {
            return Normalizer
                .normalize(name, Normalizer.Form.NFD)
                .replace("[^\\p{ASCII}]".toRegex(), "")
                .replace("[^a-zA-Z0-9\\s]+".toRegex(), "").trim()
                .replace("\\s+".toRegex(), "-")
                .toLowerCase()
        }

    }

    abstract class Repository : EntityClass<UUID, Project>(Projects, Project::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Project {
            return Project(entityId)
        }
    }

    internal class Team constructor(id: EntityID<UUID>) : UUIDEntity(id) {
        var name by Teams.name
        var project by Teams.project
        var status by Teams.status

        fun close() {
            status = io.easybreezy.project.model.team.Status.CLOSED
        }

        fun activate() {
            status = io.easybreezy.project.model.team.Status.ACTIVE
        }

        companion object : UUIDEntityClass<Team>(Teams)
    }
}


object Projects : UUIDTable("projects") {
    val name = varchar("name", 255)
    val author = uuid("author")
    val description = text("description")
    val status = enumerationByName("status", 25, Project.Status::class)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
    val slug = varchar("slug", 25)
}
