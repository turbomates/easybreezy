package io.easybreezy.project.infrastructure

import io.easybreezy.project.model.issue.Behavior
import io.easybreezy.project.model.issue.Behaviors
import io.easybreezy.project.model.issue.Issue
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class IssueRepository : Issue.Repository() {

}
