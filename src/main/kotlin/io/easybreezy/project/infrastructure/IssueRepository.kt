package io.easybreezy.project.infrastructure

import io.easybreezy.project.model.issue.Issue
import io.easybreezy.project.model.issue.Issues
import java.util.UUID

class IssueRepository : Issue.Repository() {
    fun getOne(issueId: UUID): Issue {
        return find(issueId) ?: throw NoSuchElementException("Issue with id $issueId not found")
    }

    private fun find(issueId: UUID): Issue? {
        return find { Issues.id eq issueId }.firstOrNull()
    }
}
