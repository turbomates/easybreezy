package io.easybreezy.project.model.issue

import io.easybreezy.infrastructure.event.project.issue.LabelAssigned
import io.easybreezy.infrastructure.event.project.issue.LabelCreated
import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.project.model.issue.Issue.Companion.via
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.Table
import java.time.LocalDateTime
import java.util.UUID

class Label private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    var name by Labels.name
        private set
    private var issues by Issue via IssueLabel

    companion object : PrivateEntityClass<UUID, Label>(object : Label.Repository() {}) {
        fun new(name: String): Label {
            return Label.new {
                this.name = name
                addEvent(LabelCreated(this.id.value, this.name, LocalDateTime.now()))
            }
        }
    }

    fun assignToIssue(issue: Issue) {
        val updated = issues.toMutableList()
        updated.add(issue)
        issues = SizedCollection(updated)
        addEvent(LabelAssigned(issue.id.value, id.value, LocalDateTime.now()))
    }

    abstract class Repository : EntityClass<UUID, Label>(Labels, Label::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Label {
            return Label(entityId)
        }
    }
}

object Labels : UUIDTable("labels") {
    val name = varchar("name", 25).uniqueIndex()
}

object IssueLabel : Table("issue_labels") {
    val issue = reference("issue", Issues)
    val label = reference("label", Labels)
    override val primaryKey = PrimaryKey(issue, label, name = "issue_label_pkey")
}