package io.easybreezy.project.application.issue.command.parser

import com.google.inject.Inject
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.project.application.project.queryobject.IssueStatusOnCreateQO
import java.util.UUID

class StatusWorkflow @Inject constructor(
    private val queryExecutor: QueryExecutor
) {

    suspend fun onCreate(project: UUID): UUID? {
        return queryExecutor.execute(IssueStatusOnCreateQO(project))
    }
}
