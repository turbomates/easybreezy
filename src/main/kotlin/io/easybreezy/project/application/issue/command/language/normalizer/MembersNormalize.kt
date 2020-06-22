package io.easybreezy.project.application.issue.command.language.normalizer

import com.google.inject.Inject
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.project.application.issue.command.language.NormalizedElements
import io.easybreezy.project.application.issue.command.language.ParsedElements
import io.easybreezy.project.application.project.queryobject.MembersQO
import java.util.UUID

class MembersNormalize @Inject constructor(
    private val queryExecutor: QueryExecutor
) : ElementNormalizer {

    override suspend fun normalize(project: UUID, parsed: ParsedElements, normalizedElements: NormalizedElements): NormalizedElements {
        return normalizedElements.copy(
            assignee = parsed.assignee?.let { username ->
                queryExecutor.execute(MembersQO(project, listOf(username)))?.firstOrNull()
            },
            watchers = parsed.watchers?.let { usernames ->
                queryExecutor.execute(MembersQO(project, usernames))
            }
        )
    }
}
