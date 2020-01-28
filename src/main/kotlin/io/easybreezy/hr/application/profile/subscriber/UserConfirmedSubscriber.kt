package io.easybreezy.hr.application.profile.subscriber

import com.google.inject.Inject
import io.easybreezy.hr.model.profile.PersonalData
import io.easybreezy.hr.model.profile.Profile
import io.easybreezy.hr.model.profile.Repository
import io.easybreezy.infrastructure.event.EventSubscriber
import io.easybreezy.infrastructure.event.user.Confirmed
import org.jetbrains.exposed.sql.transactions.transaction

class UserConfirmedSubscriber @Inject constructor(val repository: Repository): EventSubscriber<Confirmed> {
    override fun invoke(event: Confirmed) {

        transaction {
            val profile = repository.getByUser(event.user)

            profile.updatePersonalData(PersonalData.build {
                name = PersonalData.Name.create(event.firstName, event.lastName)
            })
        }
    }
}
