package io.easybreezy.project.application.issue.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.project.application.issue.command.language.NormalizedFields
import io.easybreezy.project.application.issue.command.language.Normalizer
import io.easybreezy.project.application.issue.command.language.Parser
import io.easybreezy.project.infrastructure.IssueRepository
import io.easybreezy.project.infrastructure.ParticipantRepository
import io.easybreezy.project.infrastructure.TimingRepository
import io.easybreezy.project.infrastructure.WorkflowRepository
import io.easybreezy.project.model.issue.Workflow
import io.easybreezy.project.model.issue.Timing
import io.easybreezy.project.model.Repository as ProjectRepository
import io.easybreezy.project.model.issue.Issue
import io.easybreezy.project.model.issue.Participant
import java.util.UUID

class Handler @Inject constructor(
    private val transaction: TransactionManager,
    private val parser: Parser,
    private val statusWorkflow: StatusWorkflowHelper,
    private val normalizer: Normalizer,
    private val repository: IssueRepository,
    private val projectRepository: ProjectRepository,
    private val workflowRepository: WorkflowRepository,
    private val participantRepository: ParticipantRepository,
    private val timingRepository: TimingRepository
) {

    private val issueFields = listOf(NormalizedFields::priority.name, NormalizedFields::category.name)

    suspend fun newIssue(command: New) {
        transaction {
            val project = projectRepository.getBySlug(command.project).id.value
            val parsed = parser.parse(command.content)
            val normalized = normalizer.normalize(parsed, project, issueFields)
            Issue.planIssue(
                command.author,
                project,
                parsed.titleDescription.title,
                parsed.titleDescription.description,
                normalized.priority,
                normalized.category
            )
        }
    }

    suspend fun update(command: AddComment) {
        transaction {
            val issue = issue(command.issue)
            val parsed = parser.parse(command.content)
            val normalized = normalizer.normalize(parsed, issue.project(), issueFields)

            issue.comment(command.member, parsed.titleDescription.description)
            normalized.category?.let {
                issue.changeCategory(it)
            }
            normalized.priority?.let {
                issue.updatePriority(it)
            }
        }
    }

    suspend fun createSubIssue(command: CreateSubIssue) {
        transaction {
            val issue = issue(command.issue)
            val parsed = parser.parse(command.content)
            val normalized = normalizer.normalize(parsed, issue.project(), issueFields)
            issue.extractSubIssue(
                command.member,
                parsed.titleDescription.title,
                parsed.titleDescription.description,
                normalized.priority,
                normalized.category
            )
        }
    }

    suspend fun startWorkflow(command: StartWorkflow) {
        statusWorkflow.getStartStatus(command.project)?.let {
            transaction {
                Workflow.ofIssue(command.issue, it)
            }
        }
    }

    suspend fun changeStatus(command: ChangeStatus) {
        transaction {
            val status = workflowRepository.findById(command.issue)
            status?.updateStatus(command.newStatus) ?: Workflow.ofIssue(command.issue, command.newStatus)
        }
    }

    suspend fun updateTiming(command: UpdateTiming) {
        val parsed = parser.parse(command.description)
        parsed.dueDate?.let {

            transaction {
                val timing = timingRepository.findById(command.issue)
                timing?.changeDueDate(it) ?: Timing.ofIssue(command.issue, it)
            }
        }
    }

    suspend fun updateLabels(command: UpdateLabels) {
        val parsed = parser.parse(command.description)
        parsed.labels?.let {
            transaction {
                val normalized = normalizer.normalize(parsed, command.project, listOf(NormalizedFields::labels.name))
                normalized.labels?.let { labels -> issue(command.issue).assignLabels(labels) }
            }
        }
    }

    suspend fun updateParticipants(command: UpdateParticipants) {
        val parsed = parser.parse(command.description)
        val normalized = normalizer.normalize(parsed, command.project, listOf(NormalizedFields::participants.name))
        normalized.participants?.let { participantsFields ->
            transaction {
                when (val participants = participantRepository.findById(command.issue)) {
                    is Participant -> {
                        participantsFields.reassigned?.let {
                            participants.reassign(it)
                        }
                        participantsFields.watchers?.let {
                            participants.addWatchers(it)
                        }
                    }
                    else -> Participant.ofIssue(command.issue, participantsFields.assignee, participantsFields.watchers)
                }
            }
        }
    }

    suspend fun assignNumber(command: AssignNumber) {
        transaction {
            issue(command.issue).assignNumber(repository.getNextIssueNumber(command.project))
        }
    }

    private fun issue(id: UUID) = repository[id]
}
