package io.easybreezy.project.application.issue.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.project.application.issue.command.language.Normalizer
import io.easybreezy.project.application.issue.command.language.Parser
import io.easybreezy.project.model.Repository as ProjectRepository
import io.easybreezy.project.model.issue.Issue
import io.easybreezy.project.model.issue.Priority

class Handler @Inject constructor(
    private val transaction: TransactionManager,
    private val parser: Parser,
    private val statusWorkflow: StatusWorkflow,
    private val normalizer: Normalizer,
    private val projectRepository: ProjectRepository
) {

    suspend fun newIssue(command: New) {

        val project = projectRepository.getBySlug(command.project).id.value
        val parsed = parser.parse(command.content)
        val normalizedIssue = normalizer.normalize(parsed, project)
        val watchers = mutableListOf(command.author)
        normalizedIssue.watchers?.let { watchers.intersect(it) }

        val issue = transaction {
        Issue.create(
                command.author,
                project,
                normalizedIssue.title,
                normalizedIssue.description,
                normalizedIssue.priority ?: Priority.neutral(),
                normalizedIssue.assignee,
                normalizedIssue.category,
                statusWorkflow.onCreate(project),
                watchers,
                parsed.start,
                parsed.due
            )
        }

        normalizedIssue.labels?.let {
            transaction {
                issue.assignLabels(it)
            }
        }
    }

    suspend fun update(command: Update) {
        throw NotImplementedError()
    }
}
