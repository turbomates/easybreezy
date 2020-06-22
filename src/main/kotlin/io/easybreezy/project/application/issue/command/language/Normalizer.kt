package io.easybreezy.project.application.issue.command.language

import com.google.inject.Inject
import io.easybreezy.project.application.issue.command.language.normalizer.ElementNormalizer
import io.easybreezy.project.model.issue.Label
import io.easybreezy.project.model.issue.Priority
import java.time.LocalDateTime
import java.util.UUID

class Normalizer @Inject constructor(
    private val elementNormalizers: Set<@JvmSuppressWildcards ElementNormalizer>
) {
    suspend fun normalize(parsed: ParsedElements, project: UUID): NormalizedElements {

        var normalized = NormalizedElements(
            parsed.titleDescription.title,
            parsed.titleDescription.description
        )
        elementNormalizers.forEach {
            normalized = it.normalize(project, parsed, normalized)
        }
        return normalized
    }
}

data class NormalizedElements(
    val title: String? = null,
    val description: String? = null,
    val due: LocalDateTime? = null,
    val priority: Priority? = null,
    val category: UUID? = null,
    val assignee: UUID? = null,
    val watchers: List<UUID>? = null,
    val labels: List<Label>? = null
)
