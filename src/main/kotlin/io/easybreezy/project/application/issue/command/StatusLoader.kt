package io.easybreezy.project.application.issue.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.project.application.project.queryobject.IssueStatusOnCreateQO
import java.util.UUID

class StatusLoader @Inject constructor(
    private val queryExecutor: QueryExecutor
) {

    suspend fun getStartStatus(project: UUID): UUID? {
        return queryExecutor.execute(IssueStatusOnCreateQO(project))
    }
}
