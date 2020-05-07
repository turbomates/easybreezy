package io.easybreezy.user.application.subscriber

import com.google.inject.Inject
import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.event.EventSubscriber
import io.easybreezy.infrastructure.event.EventsSubscriber
import io.easybreezy.infrastructure.event.employee.Fired
import io.easybreezy.user.application.command.Handler

class EmployeeSubscriber @Inject constructor(private val handler: Handler) : EventsSubscriber {
    override fun subscribers(): List<EventsSubscriber.EventSubscriberItem<out Event>> {
        return listOf(
            Fired to object : EventSubscriber<Fired> {
                override suspend fun invoke(event: Fired) {
                    handler.handleFire(event.user)
                }
            }
        )
    }
}
