package io.easybreezy.project.application.issue.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.project.application.issue.FileStorage
import io.easybreezy.project.application.issue.command.language.Normalizer
import io.easybreezy.project.application.issue.command.language.Parser
import io.easybreezy.project.infrastructure.IssueRepository
import io.easybreezy.project.model.Repository as ProjectRepository
import io.easybreezy.project.model.issue.Issue
import io.easybreezy.project.model.issue.Priority
import org.jetbrains.exposed.sql.transactions.transaction

class Handler @Inject constructor(
    private val transaction: TransactionManager,
    private val parser: Parser,
    private val statusWorkflow: StatusWorkflow,
    private val normalizer: Normalizer,
    private val projectRepository: ProjectRepository,
    private val issueRepository: IssueRepository,
    private val fileStorage: FileStorage
) {

    suspend fun newIssue(command: New) = transaction {
        val project = projectRepository.getBySlug(command.project).id.value
        val parsed = parser.parse(command.content)
        val normalizedIssue = normalizer.normalize(parsed, project)
        val watchers = mutableListOf(command.author)
        normalizedIssue.watchers?.let { watchers.intersect(it) }

        val issue = Issue.create(
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

        normalizedIssue.labels?.let { issue.assignLabels(it) }
    }

    suspend fun addFiles(command: AddFiles) = transaction {
        val issue = issueRepository.getOne(command.issueId)
        issue.addFiles(command.files, fileStorage)
    }

    suspend fun removeFile(command: RemoveFile) = transaction {
        val issue = issueRepository.getOne(command.issueId)
        issue.removeFile(command.path, fileStorage)
    }

    suspend fun update(command: Update) {
        throw NotImplementedError()
    }
}