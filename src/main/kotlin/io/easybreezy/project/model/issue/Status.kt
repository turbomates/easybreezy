package io.easybreezy.project.model.issue

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.project.model.Project
import io.easybreezy.project.model.Projects
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.*

class Status private constructor(id: EntityID<UUID>) : UUIDEntity(id) {
    var name by Statuses.name
        private set
    private var project by Project referencedOn Statuses.project

    companion object : PrivateEntityClass<UUID, Status>(object : Repository() {}) {
        fun new(project: Project, name: String): Status {
            return Status.new {
                this.project = project
                this.name = name
            }
        }
    }

    fun rename(newName: String) {
        name = newName
    }

    abstract class Repository : UUIDEntityClass<Status>(Statuses, Status::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Status {
            return Status(entityId)
        }
    }
}

object Statuses : UUIDTable("issue_statuses") {
    val project = reference("project", Projects)
    val name = varchar("name", 25)
}