package io.easybreezy.project.model.issue

import io.easybreezy.infrastructure.event.project.issue.Commented
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
    private var issue by Issue referencedOn Comments.issue
    private var comment by Comments.comment
    private var author by Comments.author
    private var createdAt by Comments.createdAt

    companion object : PrivateEntityClass<UUID, Comment>(object : Repository() {}) {
        fun add(author: UUID, issue: Issue, comment: String) = Comment.new {
            this.author = author
            this.issue = issue
            this.comment = comment
            addEvent(Commented(this.issue.id.value, this.issue.project(), author, comment, LocalDateTime.now()))
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

object Comments : UUIDTable("issue_comments") {
    val issue = reference("issue_id", Issues)
    val comment = varchar("comment", 500)
    val author = uuid("author")
    val createdAt = datetime("created_at").default(LocalDateTime.now())
}
