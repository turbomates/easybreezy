package io.easybreezy.project.application.issue.command.language

import com.google.inject.Inject
import io.easybreezy.project.application.issue.command.language.element.ElementNormalizer
import io.easybreezy.project.model.issue.Label
import io.easybreezy.project.model.issue.Priority
import java.util.UUID

class Normalizer @Inject constructor(
    private val elementNormalizers: Set<@JvmSuppressWildcards ElementNormalizer>
) {
    suspend fun normalize(parsedIssue: ParsedIssue, project: UUID): NormalizedIssue {

        var normalizedIssue = NormalizedIssue(
            parsedIssue.title,
            parsedIssue.description
        )
        elementNormalizers.forEach {
            normalizedIssue = it.normalize(project, parsedIssue, normalizedIssue)
        }
        return normalizedIssue
    }
}

data class NormalizedIssue(
    val title: String,
    val description: String,
    val priority: Priority? = null,
    val category: UUID? = null,
    val assignee: UUID? = null,
    val watchers: List<UUID>? = null,
    val labels: List<Label>? = null
)
