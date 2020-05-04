package io.easybreezy.project.application.issue.queryobject

import io.easybreezy.infrastructure.query.ContinuousList
import io.easybreezy.infrastructure.query.PagingParameters
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import io.easybreezy.project.model.Projects
import io.easybreezy.project.model.issue.Issues
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.util.*
import io.easybreezy.infrastructure.query.toContinuousList
import io.easybreezy.project.application.project.queryobject.toProject
import io.easybreezy.project.model.issue.PriorityTable

class HasIssuesInCategoryQO(private val inCategory: UUID) : QueryObject<Boolean> {
    override suspend fun getData() =
        Issues.select { Issues.category eq inCategory }.count() > 0
}

class HasIssuesInStatusQO(private val inStatus: UUID) : QueryObject<Boolean> {
    override suspend fun getData() =
        Issues.select { Issues.status eq inStatus }.count() > 0
}

class IssueQO(private val id: UUID) : QueryObject<Issue> {
    override suspend fun getData() =
        Issues
            .select {
                Issues.id eq id
            }
            .first()
            .toIssue()
}

class IssuesQO(private val paging: PagingParameters) : QueryObject<ContinuousList<Issue>> {
    override suspend fun getData() =
        Issues
            .selectAll()
            .toContinuousList(paging, ResultRow::toIssue)
}

fun ResultRow.toIssue() = Issue(
    this[Issues.id].value,
    this[Issues.title],
    this[Issues.priority[PriorityTable.color]]?.rgb
)


@Serializable
data class Issue(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val title: String,
    val priority: String?
)