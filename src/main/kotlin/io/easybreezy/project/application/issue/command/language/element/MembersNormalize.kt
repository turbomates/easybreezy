package io.easybreezy.project.application.issue.command.language.element

import com.google.inject.Inject
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.project.application.issue.command.language.NormalizedIssue
import io.easybreezy.project.application.issue.command.language.ParsedIssue
import io.easybreezy.project.application.project.queryobject.MembersQO
import java.util.UUID

class MembersNormalize @Inject constructor(
    private val queryExecutor: QueryExecutor
) : ElementNormalizer {

    override suspend fun normalize(project: UUID, parsedIssue: ParsedIssue, normalizedIssue: NormalizedIssue): NormalizedIssue {

        return normalizedIssue.copy(
            assignee = parsedIssue.assignee?.let { username ->
                queryExecutor.execute(MembersQO(project, listOf(username)))?.firstOrNull()
            },
            watchers = parsedIssue.watchers?.let { usernames ->
                queryExecutor.execute(MembersQO(project, usernames))
            }
        )
    }
}
