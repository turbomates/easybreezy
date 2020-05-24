package io.easybreezy.project.application.issue.command.language

import com.google.inject.Inject
import io.easybreezy.project.application.issue.command.language.element.CategoryNormalizer
import io.easybreezy.project.application.issue.command.language.element.LabelNormalizer
import io.easybreezy.project.application.issue.command.language.element.MembersNormalize
import io.easybreezy.project.application.issue.command.language.element.PriorityNormalizer
import io.easybreezy.project.model.issue.Label
import io.easybreezy.project.model.issue.Priority
import java.util.UUID

class Normalizer @Inject constructor(
    private val categoryConverter: CategoryNormalizer,
    private val membersNormalize: MembersNormalize,
    private val priorityConverter: PriorityNormalizer,
    private val labelConverter: LabelNormalizer
) {
    suspend fun normalize(parsedIssue: ParsedIssue, project: UUID): NormalizedIssue {

        return NormalizedIssue(
            parsedIssue.title,
            parsedIssue.description,
            priorityConverter.normalize(project, parsedIssue.priority),
            parsedIssue.category?.let { categoryConverter.normalize(project, it) },
            parsedIssue.assignee?.let { membersNormalize.normalize(project, listOf(it))?.firstOrNull() },
            parsedIssue.watchers?.let { membersNormalize.normalize(project, it) },
            parsedIssue.labels?.let { labelConverter.normalize(it) }
        )
    }
}

data class NormalizedIssue(
    val title: String,
    val description: String,
    val priority: Priority,
    val category: UUID? = null,
    val assignee: UUID? = null,
    val watchers: List<UUID>? = null,
    val labels: List<Label>? = null
)
