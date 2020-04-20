package io.easybreezy.project.application.issue.queryobject

import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.project.model.issue.Issues
import org.jetbrains.exposed.sql.select
import java.util.*

class HasIssuesInCategoryQO(private val inCategory: UUID) : QueryObject<Boolean> {
    override suspend fun getData() =
        Issues.select { Issues.category eq inCategory }.count() > 0
}

class HasIssuesInStatusQO(private val inStatus: UUID) : QueryObject<Boolean> {
    override suspend fun getData() =
        Issues.select { Issues.status eq inStatus }.count() > 0
}