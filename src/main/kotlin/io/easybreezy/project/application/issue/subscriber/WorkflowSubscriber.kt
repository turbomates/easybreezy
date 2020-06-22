package io.easybreezy.project.application.issue.subscriber

import com.google.inject.Inject
import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.event.EventSubscriber
import io.easybreezy.infrastructure.event.EventsSubscriber
import io.easybreezy.infrastructure.event.project.issue.Created
import io.easybreezy.infrastructure.event.project.issue.SubIssueCreated
import io.easybreezy.project.application.issue.command.Handler
import io.easybreezy.project.application.issue.command.ApplyWorkflow

class WorkflowSubscriber @Inject constructor(private val handler: Handler) : EventsSubscriber {
    override fun subscribers(): List<EventsSubscriber.EventSubscriberItem<out Event>> {
        return listOf(
            Created to object : EventSubscriber<Created> {
                override suspend fun invoke(event: Created) {
                    handler.applyWorkflow(ApplyWorkflow(event.issue, event.project))
                }
            },
            SubIssueCreated to object : EventSubscriber<SubIssueCreated> {
                override suspend fun invoke(event: SubIssueCreated) {
                    handler.applyWorkflow(ApplyWorkflow(event.issue, event.project))
                }
            }
        )
    }
}
