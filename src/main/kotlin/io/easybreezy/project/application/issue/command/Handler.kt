package io.easybreezy.project.application.issue.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.project.application.issue.command.parser.Parser
import io.easybreezy.project.application.issue.command.parser.StatusWorkflow
import io.easybreezy.project.application.issue.command.parser.CategoryConverter
import io.easybreezy.project.application.issue.command.parser.MembersConverter
import io.easybreezy.project.application.issue.command.parser.PriorityConverter
import io.easybreezy.project.model.Repository as ProjectRepository
import io.easybreezy.project.model.issue.Issue

class Handler @Inject constructor(
    private val transaction: TransactionManager,
    private val parser: Parser,
    private val statusWorkflow: StatusWorkflow,
    private val categoryConverter: CategoryConverter,
    private val membersConverter: MembersConverter,
    private val priorityConverter: PriorityConverter,
    private val projectRepository: ProjectRepository
) {

    suspend fun newIssue(command: New) {
        val parsed = parser.parse(command.content)
        val project = projectRepository.getBySlug(command.project).id.value
        val watchers = mutableListOf(command.author)
        if (parsed.watchers != null) {
            membersConverter.convert(project, parsed.watchers)?.let { watchers.intersect(it) }
        }

        transaction {
            Issue.create(
                command.author,
                project,
                parsed.title,
                parsed.description,
                priorityConverter.convert(project, parsed.priority),
                parsed.assignee?.let { membersConverter.convert(project, listOf(it))?.firstOrNull() },
                parsed.category?.let { categoryConverter.convert(project, it) },
                statusWorkflow.onCreate(project),
                watchers
            )
        }
    }

    suspend fun update(command: Update) {
        throw NotImplementedError()
    }
}
