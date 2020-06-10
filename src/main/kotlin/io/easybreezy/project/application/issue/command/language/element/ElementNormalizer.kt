package io.easybreezy.project.application.issue.command.language.element

import io.easybreezy.project.application.issue.command.language.NormalizedIssue
import io.easybreezy.project.application.issue.command.language.ParsedIssue
import java.util.UUID

interface ElementNormalizer {
    suspend fun normalize(project: UUID, parsedIssue: ParsedIssue, normalizedIssue: NormalizedIssue): NormalizedIssue
}
