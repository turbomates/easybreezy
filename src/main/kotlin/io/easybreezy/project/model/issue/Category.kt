package io.easybreezy.project.model.issue

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.project.model.Project
import io.easybreezy.project.model.Projects
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

class Category private constructor(id: EntityID<UUID>) : UUIDEntity(id) {
    var name by Categories.name
        private set
    private var parent by Categories.parent
    private var project by Project referencedOn Categories.project

    companion object : PrivateEntityClass<UUID, Category>(object : Repository() {}) {
        fun new(project: Project, name: String, parent: UUID? = null): Category {
            return Category.new {
                this.project = project
                this.name = name
                this.parent = parent
            }
        }
    }

    fun rename(newName: String) {
        name = newName
    }

    fun changeParent(newParent: UUID?) {
        parent = newParent
    }

    abstract class Repository : UUIDEntityClass<Category>(Categories, Category::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Category {
            return Category(entityId)
        }
    }
}

object Categories : UUIDTable("issue_categories") {
    val project = reference("project", Projects)
    val name = varchar("name", 25)
    val parent = uuid("parent").nullable()
}
