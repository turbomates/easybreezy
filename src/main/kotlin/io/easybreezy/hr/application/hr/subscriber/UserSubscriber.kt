package io.easybreezy.hr.application.hr.subscriber

import com.google.inject.Inject
import io.easybreezy.hr.application.hr.command.RegisterCard
import io.easybreezy.hr.application.hr.command.Handler
import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.event.EventSubscriber
import io.easybreezy.infrastructure.event.EventsSubscriber
import io.easybreezy.infrastructure.event.user.Hired
import io.easybreezy.infrastructure.event.user.Invited

class UserSubscriber @Inject constructor(private val handler: Handler) : EventsSubscriber {
    override fun subscribers(): List<EventsSubscriber.EventSubscriberItem<out Event>> {
        return listOf(
            Invited to object : EventSubscriber<Invited> {
                override suspend fun invoke(event: Invited) {
                    handler.registerCard(RegisterCard(event.user))
                }
            },
            Hired to object: EventSubscriber<Hired> {
                override suspend fun invoke(event: Hired) {
                    handler.registerCard(RegisterCard(event.user))
                }
            }
        )
    }
}
