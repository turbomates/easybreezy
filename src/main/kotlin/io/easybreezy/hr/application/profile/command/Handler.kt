package io.easybreezy.hr.application.profile.command

import com.google.inject.Inject
import io.easybreezy.hr.model.profile.ContactDetails
import io.easybreezy.hr.model.hr.PersonalData
import io.easybreezy.hr.model.profile.Profiles
import io.easybreezy.hr.model.profile.Repository
import io.easybreezy.infrastructure.exposed.TransactionManager
import java.time.LocalDate

class Handler @Inject constructor(private val repository: Repository, private val transactional: TransactionManager) {

    suspend fun handleUpdateMessengers(command: UpdateMessengers) {
        transactional {
            val profile = repository.getByUser(command.id)

            command.messengers.forEach {
                if (!profile.hasMessenger(it.type)) {
                    profile.addMessenger(it.type, it.username)
                } else {
                    profile.renameMessengerUsername(it.type, it.username)
                }
            }

            profile.messengers().forEach { messenger ->
                if (!command.messengers.any { it.type.toUpperCase() == messenger.type.name }) {
                    profile.removeMessenger(messenger)
                }
            }
        }
    }

    suspend fun handleUpdateContactDetails(command: UpdateContactDetails) {
        transactional {
            val profile = repository.getByUser(command.id)
            val phones = mutableSetOf<Profiles.Phone>()
            val emails = mutableSetOf<Profiles.Email>()

            command.phones.forEach {
                phones.add(Profiles.Phone(it))
            }
            command.emails.forEach {
                emails.add(Profiles.Email(it))
            }

            profile.updateContactDetails(ContactDetails.create(phones, emails))
        }
    }
}
