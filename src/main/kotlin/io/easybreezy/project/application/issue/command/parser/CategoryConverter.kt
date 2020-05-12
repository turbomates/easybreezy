package io.easybreezy.project.application.issue.command.parser

import com.google.inject.Inject
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.project.application.project.queryobject.CategoryQO
import java.util.UUID

class CategoryConverter @Inject constructor(
    private val queryExecutor: QueryExecutor
) {
    suspend fun convert(project: UUID, name: String): UUID? {
        return queryExecutor.execute(CategoryQO(project, name))
    }
}
