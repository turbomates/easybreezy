package io.easybreezy.project.application.issue.command.language.normalizer

import io.easybreezy.project.application.issue.command.language.NormalizedFields
import io.easybreezy.project.application.issue.command.language.ParsedFields
import java.util.UUID

interface ElementNormalizer {
    suspend fun normalize(project: UUID, parsed: ParsedFields, normalized: NormalizedFields): NormalizedFields
    fun elementField(): String
}
