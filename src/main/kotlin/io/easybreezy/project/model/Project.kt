package io.easybreezy.project.model

import io.easybreezy.infrastructure.event.project.project.Activated
import io.easybreezy.infrastructure.event.project.project.CategoryAdded
import io.easybreezy.infrastructure.event.project.project.CategoryChanged
import io.easybreezy.infrastructure.event.project.project.Closed
import io.easybreezy.infrastructure.event.project.project.Created
import io.easybreezy.infrastructure.event.project.project.DescriptionWritten
import io.easybreezy.infrastructure.event.project.project.RoleAdded
import io.easybreezy.infrastructure.event.project.project.RoleChanged
import io.easybreezy.infrastructure.event.project.project.RoleRemoved
import io.easybreezy.infrastructure.event.project.project.SlugChanged
import io.easybreezy.infrastructure.event.project.project.Suspended
import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.project.model.issue.Categories
import io.easybreezy.project.model.issue.Category
import io.easybreezy.project.model.team.Role
import io.easybreezy.project.model.team.Roles
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.text.Normalizer
import java.time.LocalDateTime
import java.util.UUID

class Project private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var name by Projects.name
    private var author by Projects.author
    private var slug by Projects.slug
    private var description by Projects.description
    private var status by Projects.status
    private var updatedAt by Projects.updatedAt
    private val roles by Role referrersOn Roles.project
    private val categories by Category referrersOn Categories.project

    fun writeDescription(description: String) {
        this.description = description
        addEvent(DescriptionWritten(id.value, this.updatedAt))
    }

    fun close() {
        this.status = Status.Closed
        this.updatedAt = LocalDateTime.now()
        addEvent(Closed(id.value, this.updatedAt))
    }

    fun suspend() {
        this.status = Status.Suspended
        this.updatedAt = LocalDateTime.now()
        addEvent(Suspended(id.value, this.updatedAt))
    }

    fun activate() {
        this.status = Status.Active
        this.updatedAt = LocalDateTime.now()
        addEvent(Activated(id.value, this.updatedAt))
    }

    fun addRole(name: String, permissions: List<Role.Permission>) {
        val newRole = Role.new(this, name, permissions)
        this.updatedAt = LocalDateTime.now()
        addEvent(RoleAdded(id.value, newRole.id.value, newRole.name, permissions, this.updatedAt))
    }

    fun changeRole(roleId: UUID, permissions: List<Role.Permission>, newName: String? = null) {
        val role = roles.first { it.id.value == roleId }
        newName?.let { role.rename(it) }
        role.changePermissions(permissions)
        this.updatedAt = LocalDateTime.now()
        addEvent(RoleChanged(id.value, role.id.value, role.name, permissions, this.updatedAt))
    }

    fun removeRole(roleId: UUID) {
        val role = roles.first { it.id.value == roleId }
        role.delete()
        this.updatedAt = LocalDateTime.now()
        addEvent(RoleRemoved(id.value, role.id.value, role.name, this.updatedAt))
    }

    fun addCategory(name: String, parent: UUID?) {
        val newCategory = Category.new(this, name, parent)
        this.updatedAt = LocalDateTime.now()
        addEvent(CategoryAdded(id.value, newCategory.id.value, newCategory.name, parent, this.updatedAt))
    }

    fun changeCategory(categoryId: UUID, newParent: UUID?, newName: String? = null) {
        val category = categories.first { it.id.value == categoryId }
        newName?.let { category.rename(it) }
        category.changeParent(newParent)
        this.updatedAt = LocalDateTime.now()
        addEvent(CategoryChanged(id.value, category.id.value, category.name, newParent, this.updatedAt))
    }

    fun removeCategory(categoryId: UUID) {
        val category = categories.first { it.id.value == categoryId }
        category.delete()
        this.updatedAt = LocalDateTime.now()
        addEvent(RoleRemoved(id.value, category.id.value, category.name, this.updatedAt))
    }

    fun changeSlug(new: String) {
        this.slug = new
        addEvent(SlugChanged(id.value, this.slug))
    }

    enum class Status {
        Active,
        Closed,
        Suspended
    }

    companion object : PrivateEntityClass<UUID, Project>(object : Repository() {}) {
        fun new(author: UUID, name: String, description: String, slug: String?): Project {
            return Project.new {
                this.name = name
                this.author = author
                this.status = Status.Active
                this.slug = slug?.let { slug } ?: slugify(name)
                this.description = description
                defaultRoles(this)
                addEvent(Created(this.id.value, this.name, LocalDateTime.now()))
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
}

object Projects : UUIDTable("projects") {
    val name = varchar("name", 255)
    val author = uuid("author")
    val description = text("description")
    val status = enumerationByName("status", 25, Project.Status::class)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
    val slug = varchar("slug", 25).uniqueIndex("projects_slug_idx")
}

interface Repository {
    fun getBySlug(slug: String): Project
    fun hasMembers(withRoleId: UUID): Boolean
    fun isProjectCategory(category: UUID, project: String): Boolean
}
