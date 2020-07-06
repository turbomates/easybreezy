package io.easybreezy.project.application.issue.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.project.application.issue.command.language.Normalizer
import io.easybreezy.project.application.issue.command.language.Parser
import io.easybreezy.project.application.issue.command.language.normalizer.CategoryNormalizer
import io.easybreezy.project.application.issue.command.language.normalizer.ElementNormalizer
import io.easybreezy.project.application.issue.command.language.normalizer.LabelNormalizer
import io.easybreezy.project.application.issue.command.language.normalizer.ParticipantsNormalizer
import io.easybreezy.project.application.issue.command.language.normalizer.PriorityNormalizer
import io.easybreezy.project.infrastructure.IssueRepository
import io.easybreezy.project.infrastructure.ParticipantRepository
import io.easybreezy.project.infrastructure.TimingRepository
import io.easybreezy.project.infrastructure.WorkflowRepository
import io.easybreezy.project.model.issue.Comment
import io.easybreezy.project.model.issue.Workflow
import io.easybreezy.project.model.issue.Timing
import io.easybreezy.project.model.Repository as ProjectRepository
import io.easybreezy.project.model.issue.Issue
import io.easybreezy.project.model.issue.Participant
import java.util.UUID

class Handler @Inject constructor(
    private val transaction: TransactionManager,
    private val parser: Parser,
    private val statusWorkflow: StatusLoader,
    private val fieldNormalizers: Set<@JvmSuppressWildcards ElementNormalizer>,
    private val repository: IssueRepository,
    private val projectRepository: ProjectRepository,
    private val workflowRepository: WorkflowRepository,
    private val participantRepository: ParticipantRepository,
    private val timingRepository: TimingRepository
) {
    suspend fun newIssue(command: New) {
        transaction {
            val project = projectRepository.getBySlug(command.project).id.value
            val parsed = parser.parse(command.content)
            val normalizer = Normalizer(fieldNormalizers.filter { it is PriorityNormalizer || it is CategoryNormalizer })
            val normalized = normalizer.normalize(parsed, project)
            Issue.planIssue(
                command.author,
                project,
                parsed.titleDescription.title,
                parsed.titleDescription.description,
                repository.getNextIssueNumber(project),
                normalized.priority,
                normalized.category
            )
        }
    }

    suspend fun addComment(command: AddComment) {
        transaction {
            val issue = issue(command.issue)
            val parsed = parser.parse(command.content)
            val normalizer = Normalizer(fieldNormalizers.filter { it is PriorityNormalizer || it is CategoryNormalizer })
            val normalized = normalizer.normalize(parsed, issue.project())

            Comment.add(command.member, issue, parsed.titleDescription.description)

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
            val normalizer = Normalizer(fieldNormalizers.filter { it is PriorityNormalizer || it is CategoryNormalizer })
            val normalized = normalizer.normalize(parsed, issue.project())
            issue.extractSubIssue(
                command.member,
                parsed.titleDescription.title,
                parsed.titleDescription.description,
                repository.getNextIssueNumber(issue.project()),
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
                val normalizer = Normalizer(fieldNormalizers.filterIsInstance<LabelNormalizer>())
                val normalized = normalizer.normalize(parsed, command.project)
                normalized.labels?.let { labels -> issue(command.issue).assignLabels(labels) }
            }
        }
    }

    suspend fun updateParticipants(command: UpdateParticipants) {
        val parsed = parser.parse(command.description)
        val normalizer = Normalizer(fieldNormalizers.filterIsInstance<ParticipantsNormalizer>())
        val normalized = normalizer.normalize(parsed, command.project)
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

    private fun issue(id: UUID) = repository[id]
}
