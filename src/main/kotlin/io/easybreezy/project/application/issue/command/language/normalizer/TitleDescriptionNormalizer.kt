package io.easybreezy.project.application.issue.command.language.normalizer

import com.google.inject.Inject
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.project.application.issue.command.language.ParsedElements
import io.easybreezy.project.application.issue.command.language.NormalizedElements
import io.easybreezy.project.application.project.queryobject.CategoryQO
import java.util.UUID

class TitleDescriptionNormalizer @Inject constructor() : ElementNormalizer {
    override suspend fun normalize(project: UUID, parsed: ParsedElements, normalizedElements: NormalizedElements): NormalizedElements {

        return normalizedElements.copy(
            title = parsed.titleDescription.title,
            description = parsed.titleDescription.description
        )
    }
}
