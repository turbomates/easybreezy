package io.easybreezy.hr.model.hr

import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime
import java.util.UUID

class Comment private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var ownerId by Comments.ownerId
    private var userId by Comments.userId
    private var text by Comments.text
    private var createdAt by Comments.createdAt
    private var updatedAt by Comments.updatedAt

    companion object : PrivateEntityClass<UUID, Comment>(object : Repository() {}) {
        fun create(ownerId: UUID, userId: UUID, text: String) = Comment.new {
            this.ownerId = ownerId
            this.userId = userId
            this.text = text
            this.createdAt = LocalDateTime.now()
            this.updatedAt = LocalDateTime.now()
        }
    }

    abstract class Repository : EntityClass<UUID, Comment>(
        Comments, Comment::class.java
    ) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Comment {
            return Comment(entityId)
        }
    }
}

object Comments : UUIDTable() {
    val ownerId = uuid("owner_id")
    val userId = uuid("user_id")
    val text = text("text")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}
