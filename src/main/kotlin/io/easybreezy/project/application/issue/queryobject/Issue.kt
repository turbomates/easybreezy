@file:UseSerializers(UUIDSerializer::class)
package io.easybreezy.project.application.issue.queryobject

import io.easybreezy.infrastructure.query.ContinuousList
import io.easybreezy.infrastructure.query.PagingParameters
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.query.toContinuousList
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID
import io.easybreezy.project.model.Projects
import io.easybreezy.project.model.issue.Workflows
import io.easybreezy.project.model.issue.Categories
import io.easybreezy.project.model.issue.Comments
import io.easybreezy.project.model.issue.Timings
import io.easybreezy.project.model.issue.IssueLabel
import io.easybreezy.project.model.issue.Issues
import io.easybreezy.project.model.issue.Labels
import io.easybreezy.project.model.issue.Participants
import io.easybreezy.project.model.issue.PriorityTable
import io.easybreezy.project.model.issue.Statuses
import kotlinx.serialization.UseSerializers
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

class HasIssuesInCategoryQO(private val inCategory: UUID) : QueryObject<Boolean> {
    override suspend fun getData() =
        Issues.select { Issues.category eq inCategory }.count() > 0
}

class HasIssuesInStatusQO(private val inStatus: UUID) : QueryObject<Boolean> {
    override suspend fun getData() =
        Workflows.select { Workflows.status eq inStatus }.count() > 0
}

class IssueQO(private val id: UUID) : QueryObject<IssueDetails> {
    override suspend fun getData() =
        Issues
            .leftJoin(IssueLabel)
            .leftJoin(Comments)
            .join(Labels, JoinType.LEFT, IssueLabel.label, Labels.id)
            .join(Workflows, JoinType.LEFT, Workflows.id, Issues.id)
            .join(Timings, JoinType.LEFT, Timings.id, Issues.id)
            .join(Participants, JoinType.LEFT, Participants.id, Issues.id)
            .join(Categories, JoinType.LEFT, Issues.category, Categories.id)
            .join(Statuses, JoinType.LEFT, Workflows.status, Statuses.id)
            .select {
                Issues.id eq id
            }
            .toIssueDetails()
            .single()
}

fun Iterable<ResultRow>.toIssueDetails(): List<IssueDetails> {
    return fold(mutableMapOf<UUID, IssueDetails>()) { map, resultRow ->
        val details = resultRow.toIssueDetails()
        val current = map.getOrDefault(details.id, details)

        val labelId = resultRow.getOrNull(Labels.id)
        val labels = labelId?.let { resultRow.toLabel() }

        val categoryId = resultRow.getOrNull(Categories.id)
        val category = categoryId?.let { resultRow.toCategory() }

        val statusId = resultRow.getOrNull(Statuses.id)
        val status = statusId?.let { resultRow.toStatus() }

        val commentId = resultRow.getOrNull(Comments.id)
        val comments = commentId?.let { resultRow.toComment() }

        map[details.id] = current.copy(
            labels = current.labels.plus(listOfNotNull(labels)).distinct(),
            category = category,
            status = status,
            comments = current.comments.plus(listOfNotNull(comments)).distinct()
        )
        map
    }.values.toList()
}

class IssuesQO(private val paging: PagingParameters, private val project: String) : QueryObject<ContinuousList<Issue>> {
    override suspend fun getData() =
        Issues
            .join(Projects, JoinType.INNER, Issues.project, Projects.id)
            .select {
                Projects.slug eq project
            }
            .orderBy(Issues.priority[PriorityTable.value] to SortOrder.DESC, Issues.createdAt to SortOrder.DESC)
            .toContinuousList(paging, ResultRow::toIssue)
}

fun ResultRow.toIssue() = Issue(
    this[Issues.id].value,
    this[Issues.parent]?.value,
    this[Issues.title],
    this[Issues.priority[PriorityTable.color]]?.rgb
)

fun ResultRow.toIssueDetails() = IssueDetails(
    this[Issues.id].value,
    this[Issues.parent]?.value,
    this[Participants.assignee],
    this[Participants.watchers],
    this[Issues.title],
    this[Issues.priority[PriorityTable.color]]?.rgb
)

fun ResultRow.toLabel() = Label(
    this[Labels.id].value,
    this[Labels.name]
)

fun ResultRow.toComment() = Comment(
    this[Comments.id].value,
    this[Comments.author],
    this[Comments.comment]
)

fun ResultRow.toCategory() = Category(
    this[Categories.id].value,
    this[Categories.name]
)

fun ResultRow.toStatus() = Status(
    this[Statuses.id].value,
    this[Statuses.name]
)

@Serializable
data class Comment(
    val id: UUID,
    val author: UUID,
    val comment: String
)

@Serializable
data class Label(
    val id: UUID,
    val name: String
)

@Serializable
data class Category(
    val id: UUID,
    val name: String
)

@Serializable
data class Status(
    val id: UUID,
    val name: String
)

@Serializable
data class Issue(
    val id: UUID,
    val parent: UUID?,
    val title: String,
    val priority: String?
)

@Serializable
data class IssueDetails(
    val id: UUID,
    val parent: UUID?,
    val assignee: UUID?,
    val watchers: List<UUID>?,
    val title: String,
    val priority: String?,
    val labels: List<Label> = listOf(),
    val category: Category? = null,
    val status: Status? = null,
    val comments: List<Comment> = listOf()
)
