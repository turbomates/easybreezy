package io.easybreezy.project.application.issue.command.language.element

import com.google.inject.Inject
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.project.application.project.queryobject.LastHighestPriorityQO
import io.easybreezy.project.application.project.queryobject.LastLowestPriorityQO
import io.easybreezy.project.model.issue.Priority
import java.util.UUID

class PriorityNormalizer @Inject constructor(
    private val queryExecutor: QueryExecutor
) {
    suspend fun normalize(project: UUID, title: String?): Priority {
        return when (title?.toLowerCase()) {
            "high" -> Priority.high()
            "low" -> Priority.low()
            "normal" -> Priority.normal()
            "highest" -> Priority.highest(queryExecutor.execute(LastLowestPriorityQO(project)))
            "lowest" -> Priority.lowest(queryExecutor.execute(LastHighestPriorityQO(project)))
            else -> Priority.neutral()
        }
    }
}
