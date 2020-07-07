package io.easybreezy.project.application.issue.command.language.normalizer

import com.google.inject.Inject
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.project.application.issue.command.language.NormalizedFields
import io.easybreezy.project.application.issue.command.language.ParsedFields
import io.easybreezy.project.application.issue.command.language.ParticipantsFields
import io.easybreezy.project.application.project.queryobject.MembersQO
import java.util.UUID

class ParticipantsNormalizer @Inject constructor(
    private val queryExecutor: QueryExecutor
) : ElementNormalizer {

    override suspend fun normalize(project: UUID, parsed: ParsedFields, normalized: NormalizedFields): NormalizedFields {

        if (parsed.assignee != null || parsed.reassigned != null || parsed.watchers.count() > 0) {
            val participantsFields = ParticipantsFields(
                assignee = parsed.assignee?.let { username ->
                    queryExecutor.execute(MembersQO(project, listOf(username)))?.firstOrNull()
                },
                reassigned = parsed.reassigned?.let { username ->
                    queryExecutor.execute(MembersQO(project, listOf(username)))?.firstOrNull()
                },
                watchers = parsed.watchers.let { usernames ->
                    queryExecutor.execute(MembersQO(project, usernames))
                }
            )
            return normalized.copy(participants = participantsFields)
        }
        return normalized
    }
}
