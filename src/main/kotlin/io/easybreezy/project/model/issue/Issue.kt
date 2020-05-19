package io.easybreezy.project.model.issue

import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.infrastructure.exposed.dao.embedded
import io.easybreezy.infrastructure.exposed.type.jsonb
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.builtins.list
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime
import java.util.UUID

class Issue private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var assignee by Issues.assignee
    private var category by Issues.category
    private var author by Issues.author
    private var updatedAt by Issues.updatedAt
    private var project by Issues.project
    private var title by Issues.title
    private var description by Issues.description
    private var watchers by Issues.watchers
    private var status by Issues.status
    private var priority by Issues.priority
    private var startDate by Issues.startDate
    private var dueDate by Issues.dueDate
    private var labels by Label via IssueLabel

    companion object : PrivateEntityClass<UUID, Issue>(object : Issue.Repository() {}) {
        fun create(
            author: UUID,
            project: UUID,
            title: String,
            description: String,
            priority: Priority = Priority.normal(),
            assignee: UUID? = null,
            category: UUID? = null,
            status: UUID? = null,
            watchers: List<UUID> = listOf(),
            startDate: LocalDateTime? = null,
            dueDate: LocalDateTime? = null
        ): Issue {
            return Issue.new {
                this.author = author
                this.project = project
                this.title = title
                this.description = description
                this.assignee = assignee
                this.category = category
                this.watchers = watchers
                this.status = status
                this.priority = priority
                this.dueDate = dueDate
                this.startDate = startDate
            }
        }
    }

    fun assignLabels(list: List<Label>) {
        val updated = labels.toMutableList()
        updated.addAll(list)
        labels = SizedCollection(updated)
    }

    fun reassign(reassigned: UUID) {
        assignee = reassigned
        updatedAt = LocalDateTime.now()
    }

    abstract class Repository : EntityClass<UUID, Issue>(Issues, Issue::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Issue {
            return Issue(entityId)
        }
    }
}

object Issues : UUIDTable("issues") {
    val category = uuid("category").nullable()
    val assignee = uuid("assignee").nullable()
    val author = uuid("author")
    val project = uuid("project")
    val title = varchar("title", 255)
    val description = text("description")
    val watchers = jsonb("watchers", UUIDSerializer.list)
    val status = uuid("status").nullable()
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
    val priority = embedded<Priority>(PriorityTable)
    val startDate = datetime("start_date").nullable()
    val dueDate = datetime("due_date").nullable()
}

object IssueLabel : Table("issue_label") {
    val issue = reference("issue", Issues)
    val label = reference("label", Labels)
    override val primaryKey = PrimaryKey(issue, label, name = "issue_label_pkey")
}
