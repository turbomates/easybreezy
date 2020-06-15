package io.easybreezy.project.application.issue.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.project.application.issue.command.language.NormalizedIssue
import io.easybreezy.project.application.issue.command.language.Normalizer
import io.easybreezy.project.application.issue.command.language.Parser
import io.easybreezy.project.infrastructure.IssueRepository
import io.easybreezy.project.model.issue.Behavior
import io.easybreezy.project.model.issue.Estimation
import io.easybreezy.project.model.Repository as ProjectRepository
import io.easybreezy.project.model.issue.Issue
import io.easybreezy.project.model.issue.Participant
import java.util.UUID

class Handler @Inject constructor(
    private val transaction: TransactionManager,
    private val parser: Parser,
    private val statusWorkflow: StatusWorkflow,
    private val normalizer: Normalizer,
    private val repository: IssueRepository,
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
            statusOnCreate(project)?.let {
                Behavior.ofIssue(issue.id.value, it)
            }
            if (normalized.due != null) {
                Estimation.ofIssue(issue.id.value, normalized.due)
            }
            if (normalized.assignee != null) {
                Participant.ofIssue(issue.id.value, normalized.assignee, normalized.watchers)
            }
            issue
        }

        normalized.labels?.let {
            transaction {
                issue.assignLabels(it)
            }
        }
    }

    suspend fun update(command: AddComment) {
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

//            val solution = solution(command.issue)
//            normalized.assignee?.let {
//                solution.reassign(it)
//            }
//            normalized.watchers?.let {
//                solution.addWatchers(it)
//            }
//            normalized.due?.let { solution.changeDueDate(it) }
        }
    }

    suspend fun changeStatus(command: ChangeStatus) {
        transaction {
//            val solution = solution(command.issue)
//            solution.updateStatus(command.newStatus)
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
            statusOnCreate(issue.projectUUID())?.let {
                Behavior.ofIssue(subIssue.id.value, it)
            }
            if (normalized.due != null) {
                Estimation.ofIssue(subIssue.id.value, normalized.due)
            }
            if (normalized.assignee != null) {
                Participant.ofIssue(subIssue.id.value, normalized.assignee, normalized.watchers)
            }
        }
    }

    private suspend fun statusOnCreate(project: UUID) = statusWorkflow.onCreate(project)

    private fun issue(id: UUID) = repository[id]
}
