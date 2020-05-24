package io.easybreezy.project.application.issue.command.language.element

import com.google.inject.Inject
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.project.application.project.queryobject.MembersQO
import java.util.UUID

class MembersNormalize @Inject constructor(
    private val queryExecutor: QueryExecutor
) {
    suspend fun normalize(project: UUID, usernames: List<String>): List<UUID>? {
        return queryExecutor.execute(MembersQO(project, usernames))
    }
}
