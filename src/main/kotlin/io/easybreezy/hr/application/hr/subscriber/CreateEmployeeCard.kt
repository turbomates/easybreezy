package io.easybreezy.hr.application.hr.subscriber

import io.easybreezy.hr.model.hr.Employee
import io.easybreezy.hr.model.hr.PersonalData
import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.event.EventSubscriber
import io.easybreezy.infrastructure.event.EventsSubscriber
import io.easybreezy.infrastructure.event.user.Confirmed
import org.jetbrains.exposed.sql.transactions.transaction

class CreateEmployeeCard: EventsSubscriber {
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
            Employee.createCard(
                event.user,
                PersonalData.create(PersonalData.Name.create(event.firstName, event.lastName))
            )
        }
    }
}