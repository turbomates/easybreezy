package io.easybreezy.project.application.issue.command.parser

import com.google.inject.Inject
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.project.application.project.queryobject.MembersQO
import java.util.UUID

class MembersConverter @Inject constructor(
    private val queryExecutor: QueryExecutor
) {
    suspend fun convert(project: UUID, usernames: List<String>): List<UUID>? {
        return queryExecutor.execute(MembersQO(project, usernames))
    }
}
