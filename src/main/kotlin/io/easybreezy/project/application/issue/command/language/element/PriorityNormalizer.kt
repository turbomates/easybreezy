package io.easybreezy.project.application.issue.command.language.element

import com.google.inject.Inject
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.project.application.issue.command.language.NormalizedIssue
import io.easybreezy.project.application.issue.command.language.ParsedIssue
import io.easybreezy.project.application.project.queryobject.LastHighestPriorityQO
import io.easybreezy.project.application.project.queryobject.LastLowestPriorityQO
import io.easybreezy.project.model.issue.Priority
import java.util.UUID

class PriorityNormalizer @Inject constructor(
    private val queryExecutor: QueryExecutor
) : ElementNormalizer {

    override suspend fun normalize(project: UUID, parsedIssue: ParsedIssue, normalizedIssue: NormalizedIssue): NormalizedIssue {
        return normalizedIssue.copy(
            priority = when (parsedIssue.priority?.toLowerCase()) {
                "high" -> Priority.high()
                "low" -> Priority.low()
                "normal" -> Priority.normal()
                "highest" -> Priority.highest(queryExecutor.execute(LastLowestPriorityQO(project)))
                "lowest" -> Priority.lowest(queryExecutor.execute(LastHighestPriorityQO(project)))
                else -> null
            }
        )
    }
}
