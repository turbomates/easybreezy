package io.easybreezy.project.application.issue.command.language.element

import com.google.inject.Inject
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.project.application.issue.command.language.NormalizedIssue
import io.easybreezy.project.application.issue.command.language.ParsedIssue
import io.easybreezy.project.application.project.queryobject.CategoryQO
import java.util.UUID

class CategoryNormalizer @Inject constructor(
    private val queryExecutor: QueryExecutor
) : ElementNormalizer {
    override suspend fun normalize(project: UUID, parsedIssue: ParsedIssue, normalizedIssue: NormalizedIssue): NormalizedIssue {

        return normalizedIssue.copy(
            category = parsedIssue.category?.let { name ->
                queryExecutor.execute(CategoryQO(project, name))
            }
        )
    }
}
