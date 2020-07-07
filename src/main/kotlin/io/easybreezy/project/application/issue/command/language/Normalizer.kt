package io.easybreezy.project.application.issue.command.language

import com.google.inject.Inject
import io.easybreezy.project.application.issue.command.language.normalizer.ElementNormalizer
import io.easybreezy.project.model.issue.Label
import io.easybreezy.project.model.issue.Priority
import java.util.UUID

class Normalizer @Inject constructor(
    private val fieldNormalizers: List<ElementNormalizer>
) {
    suspend fun normalize(parsed: ParsedFields, project: UUID): NormalizedFields {
        var normalized = NormalizedFields()
        fieldNormalizers.forEach {
            normalized = it.normalize(project, parsed, normalized)
        }

        return normalized
    }
}

data class NormalizedFields(
    val priority: Priority? = null,
    val category: UUID? = null,
    val labels: List<Label>? = null,
    val participants: ParticipantsFields? = null
)

data class ParticipantsFields(
    val assignee: UUID? = null,
    val reassigned: UUID? = null,
    val watchers: List<UUID>? = null
)
