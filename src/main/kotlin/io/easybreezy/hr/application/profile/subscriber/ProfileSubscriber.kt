package io.easybreezy.hr.application.profile.subscriber

import io.easybreezy.hr.model.profile.PersonalData
import io.easybreezy.hr.model.profile.Profile
import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.event.EventSubscriber
import io.easybreezy.infrastructure.event.EventsSubscriber
import io.easybreezy.infrastructure.event.user.Confirmed
import org.jetbrains.exposed.sql.transactions.transaction

class ProfileSubscriber : EventsSubscriber {
    override fun subscribers(): List<EventsSubscriber.EventSubscriberItem<out Event>> {
        return listOf(
            Confirmed to object : EventSubscriber<Confirmed> {
                override fun invoke(event: Confirmed) {
                    confirm(event)
                }
            }
        )
    }

    private fun confirm(event: Confirmed) {
        transaction {
            Profile.create(
                event.user,
                PersonalData.create(PersonalData.Name.create(event.firstName, event.lastName))
            )
        }
    }
}
