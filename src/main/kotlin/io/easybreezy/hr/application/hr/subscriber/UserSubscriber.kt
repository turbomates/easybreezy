package io.easybreezy.hr.application.hr.subscriber

import com.google.inject.Inject
import io.easybreezy.hr.model.hr.Employee
import io.easybreezy.hr.model.hr.Employees
import io.easybreezy.hr.model.hr.PersonalData
import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.event.EventSubscriber
import io.easybreezy.infrastructure.event.EventsSubscriber
import io.easybreezy.infrastructure.event.user.Confirmed
import io.easybreezy.infrastructure.event.user.Invited
import io.easybreezy.infrastructure.exposed.TransactionManager
import org.jetbrains.exposed.sql.insert

class UserSubscriber @Inject constructor(private val transaction: TransactionManager) : EventsSubscriber {
    override fun subscribers(): List<EventsSubscriber.EventSubscriberItem<out Event>> {
        return listOf(
            Confirmed to object : EventSubscriber<Confirmed> {
                override suspend fun invoke(event: Confirmed) {
                    confirm(event)
                }
            },
            Invited to object : EventSubscriber<Invited> {
                override suspend fun invoke(event: Invited) {
                    createEmployee(event)
                }
            }
        )
    }

    private suspend fun confirm(event: Confirmed) {
        transaction {
            Employee.createCard(
                event.user,
                PersonalData.create()
            )
        }
    }

    private suspend fun createEmployee(event: Invited) {
        transaction {
            Employees.insert {
                it[userId] = event.user
            }
        }
    }
}
