package io.easybreezy.project.application.issue.command.language.normalizer

import com.google.inject.Inject
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.project.application.issue.command.language.NormalizedFields
import io.easybreezy.project.application.issue.command.language.ParsedFields
import io.easybreezy.project.application.project.queryobject.CategoryQO
import java.util.UUID

class CategoryNormalizer @Inject constructor(
    private val queryExecutor: QueryExecutor
) : ElementNormalizer {
    override suspend fun normalize(project: UUID, parsed: ParsedFields, normalized: NormalizedFields): NormalizedFields {

        return normalized.copy(
            category = parsed.category?.let { name ->
                queryExecutor.execute(CategoryQO(project, name))
            }
        )
    }
}
