package io.easybreezy.project.model.team

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.*

class Role private constructor(id: EntityID<UUID>) : UUIDEntity(id) {
    private var project by Roles.project
    private var name by Roles.name
    private val members by Member referrersOn Members.role

    companion object : PrivateEntityClass<UUID, Role>(RoleRepository) {
        fun create(project: UUID, name: String): Role {
            return Role.new {
                this.project = project
                this.name = name
            }
        }

    }

    abstract class Repository : UUIDEntityClass<Role>(Roles, Role::class.java) {
        protected override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Role {
            return Role(entityId)
        }
    }
}

object RoleRepository : Role.Repository() {

}

object Roles : UUIDTable() {
    val project = uuid("project")
    val name = varchar("name", 25)
}