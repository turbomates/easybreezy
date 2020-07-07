package io.easybreezy.project.infrastructure

import io.easybreezy.project.model.issue.Issue
import io.easybreezy.project.model.issue.Issues
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.max
import org.jetbrains.exposed.sql.select
import java.util.UUID

class IssueRepository : Issue.Repository() {
    fun getNextIssueNumber(project: UUID): Int {
        val row = Issues
            .slice(Issues.number.max())
            .select { Issues.project eq project }
            .groupBy(Issues.project)
            .map { row -> row.getOrNull(Issues.number.max() as Expression<Int>) }
            .firstOrNull()

        return row?.plus(1) ?: 1
    }
}
