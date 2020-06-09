package io.easybreezy.project.application.issue.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.project.application.issue.command.language.NormalizedIssue
import io.easybreezy.project.application.issue.command.language.Normalizer
import io.easybreezy.project.application.issue.command.language.Parser
import io.easybreezy.project.infrastructure.IssueRepository
import io.easybreezy.project.infrastructure.SolutionRepository
import io.easybreezy.project.model.Repository as ProjectRepository
import io.easybreezy.project.model.issue.Issue
import io.easybreezy.project.model.issue.Solution
import java.util.UUID

class Handler @Inject constructor(
    private val transaction: TransactionManager,
    private val parser: Parser,
    private val statusWorkflow: StatusWorkflow,
    private val normalizer: Normalizer,
    private val repository: IssueRepository,
    private val solutionRepository: SolutionRepository,
    private val projectRepository: ProjectRepository
) {

    suspend fun newIssue(command: New) {
        val project = projectRepository.getBySlug(command.project).id.value
        val normalized = normalizer.normalize(parser.parse(command.content), project)
        val issue = transaction {
            val issue = Issue.planIssue(
                command.author,
                project,
                normalized.title,
                normalized.description,
                normalized.priority,
                normalized.category
            )
            createSolutionOfIssue(issue, normalized, project)
            issue
        }

        normalized.labels?.let {
            transaction {
                issue.assignLabels(it)
            }
        }
    }

    suspend fun update(command: CommentUpdate) {
        transaction {
            val issue = issue(command.issue)
            val normalized = normalizer.normalize(parser.parse(command.content), issue.projectUUID())

            issue.comment(command.member, normalized.description)
            normalized.category?.let {
                issue.changeCategory(it)
            }
            normalized.labels?.let {
                issue.assignLabels(it)
            }
            normalized.priority?.let {
                issue.updatePriority(it)
            }

            val solution = solution(command.issue)
            normalized.assignee?.let {
                solution.reassign(it)
            }
            normalized.watchers?.let {
                solution.updateWatchers(it)
            }
            normalized.due?.let { solution.changeDueDate(it) }
        }
    }

    suspend fun changeStatus(command: ChangeStatus) {
        transaction {
            val solution = solution(command.issue)
            solution.updateStatus(command.newStatus)
        }
    }

    suspend fun createSubIssue(command: CreateSubIssue) {
        transaction {
            val issue = issue(command.issue)
            val normalized = normalizer.normalize(parser.parse(command.content), issue.projectUUID())
            val subIssue = issue.extractSubIssue(
                command.member,
                normalized.title,
                normalized.description,
                normalized.priority,
                normalized.category
            )
            createSolutionOfIssue(subIssue, normalized, issue.projectUUID())
        }
    }

    private suspend fun createSolutionOfIssue(issue: Issue, normalized: NormalizedIssue, project: UUID) =
        Solution.ofIssue(
            issue.id.value,
            normalized.assignee,
            statusOnCreate(project),
            normalized.watchers,
            normalized.due
        )

    private suspend fun statusOnCreate(project: UUID) = statusWorkflow.onCreate(project)

    private fun issue(id: UUID) = repository[id]

    private fun solution(id: UUID) = solutionRepository[id]
}
