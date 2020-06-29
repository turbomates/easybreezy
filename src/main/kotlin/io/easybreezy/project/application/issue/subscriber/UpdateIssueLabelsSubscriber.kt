package io.easybreezy.project.application.issue.subscriber

import com.google.inject.Inject
import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.event.EventSubscriber
import io.easybreezy.infrastructure.event.EventsSubscriber
import io.easybreezy.infrastructure.event.project.issue.Commented
import io.easybreezy.infrastructure.event.project.issue.Created
import io.easybreezy.infrastructure.event.project.issue.SubIssueCreated
import io.easybreezy.project.application.issue.command.UpdateLabels
import io.easybreezy.project.application.issue.command.Handler

class UpdateIssueLabelsSubscriber @Inject constructor(private val handler: Handler) : EventsSubscriber {
    override fun subscribers(): List<EventsSubscriber.EventSubscriberItem<out Event>> {
        return listOf(
            Created to object : EventSubscriber<Created> {
                override suspend fun invoke(event: Created) {
                    if (null !== event.description) {
                        handler.updateLabels(UpdateLabels(event.issue, event.project, event.description))
                    }
                }
            },
            SubIssueCreated to object : EventSubscriber<SubIssueCreated> {
                override suspend fun invoke(event: SubIssueCreated) {
                    if (null !== event.description) {
                        handler.updateLabels(UpdateLabels(event.issue, event.project, event.description))
                    }
                }
            },
            Commented to object : EventSubscriber<Commented> {
                override suspend fun invoke(event: Commented) {
                    handler.updateLabels(UpdateLabels(event.issue, event.project, event.content))
                }
            }
        )
    }
}
