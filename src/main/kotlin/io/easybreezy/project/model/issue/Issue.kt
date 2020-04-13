package io.easybreezy.project.model.issue

import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime
import java.util.UUID

class Issue private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    companion object : PrivateEntityClass<UUID, Issue>(object : Issue.Repository() {}) {
        fun create(name: String, project: UUID): Issue {
            return Issue.new {


            }
        }
    }

    abstract class Repository : EntityClass<UUID, Issue>(Issues, Issue::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Issue {
            return Issue(entityId)
        }
    }
}

object Issues : UUIDTable("issues") {
    val category = uuid("category")
//    val name = Issues.varchar("name", 25)
//    val project = Issues.uuid("project")
//    val status = Issues.enumerationByName("status", 25, Status::class).default(Status.Active)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}