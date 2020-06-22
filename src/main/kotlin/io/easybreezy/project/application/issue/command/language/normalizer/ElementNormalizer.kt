package io.easybreezy.project.application.issue.command.language.normalizer

import io.easybreezy.project.application.issue.command.language.NormalizedElements
import io.easybreezy.project.application.issue.command.language.ParsedElements
import java.util.UUID

interface ElementNormalizer {
    suspend fun normalize(project: UUID, parsed: ParsedElements, normalizedElements: NormalizedElements): NormalizedElements
}
