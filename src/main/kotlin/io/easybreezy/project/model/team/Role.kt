package io.easybreezy.project.model.team

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.infrastructure.exposed.type.jsonb
import io.easybreezy.project.model.Project
import io.easybreezy.project.model.Projects
import kotlinx.serialization.builtins.list
import kotlinx.serialization.serializer
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

class Role private constructor(id: EntityID<UUID>) : UUIDEntity(id) {
    var name by Roles.name
        private set
    private var permissions by Roles.permissions
    private var project by Project referencedOn Roles.project

    fun rename(name: String) {
        this.name = name
    }

    fun changePermissions(permissions: List<String>) {
        this.permissions = permissions
    }

    companion object : PrivateEntityClass<UUID, Role>(object : Repository() {}) {
        fun new(project: Project, name: String, permissions: List<String>): Role {
            return Role.new {
                this.project = project
                this.name = name
                this.permissions = permissions
            }
        }
    }

    abstract class Repository : UUIDEntityClass<Role>(Roles, Role::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Role {
            return Role(entityId)
        }
    }
}

object Roles : UUIDTable("project_roles") {
    val project = reference("project", Projects)
    val name = varchar("name", 25)
    val permissions = jsonb("permissions", String::class.serializer().list)
}
