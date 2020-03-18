package io.easybreezy.hr.application.hr.subscriber

import io.easybreezy.hr.model.hr.Employee
import io.easybreezy.hr.model.hr.Employees
import io.easybreezy.hr.model.hr.PersonalData
import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.event.EventSubscriber
import io.easybreezy.infrastructure.event.EventsSubscriber
import io.easybreezy.infrastructure.event.user.Confirmed
import io.easybreezy.infrastructure.event.user.Invited
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class UserSubscriber : EventsSubscriber {
    override fun subscribers(): List<EventsSubscriber.EventSubscriberItem<out Event>> {
        return listOf(
            Confirmed to object : EventSubscriber<Confirmed> {
                override fun invoke(event: Confirmed) {
                    confirm(event)
                }
            },
            Invited to object : EventSubscriber<Invited> {
                override fun invoke(event: Invited) {
                    createEmployee(event)
                }
            }
        )
    }

    private fun confirm(event: Confirmed) {
        transaction {
            Employee.createCard(
                event.user,
                PersonalData.create()
            )
        }
    }

    private fun createEmployee(event: Invited) {
        transaction {
            Employees.insert {
                it[userId] = event.user
            }
        }
    }
}
