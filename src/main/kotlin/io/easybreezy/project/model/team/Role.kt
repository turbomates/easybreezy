package io.easybreezy.project.model.team

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.infrastructure.exposed.type.jsonb
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.*

class Role private constructor(id: EntityID<UUID>) : UUIDEntity(id) {
    private var project by Roles.project
    var name by Roles.name
        private set
    private var permissions by Roles.permissions
    fun membersCount(): Int {
        return 0
    }

    fun rename(name: String) {
        this.name = name
    }

    fun changePermissions(permissions: List<String>) {
        this.permissions = permissions
    }

    companion object : PrivateEntityClass<UUID, Role>(object : Repository() {}) {
        fun new(project: UUID, name: String, permissions: List<String>): Role {
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

object Roles : UUIDTable() {
    val project = uuid("project")
    val name = varchar("name", 25)
    val permissions = jsonb("permissions", String.serializer().list)
}