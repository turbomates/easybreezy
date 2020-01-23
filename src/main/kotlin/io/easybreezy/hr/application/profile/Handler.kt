package io.easybreezy.hr.application.profile

import com.google.inject.Inject
import io.easybreezy.hr.model.profile.Profile
import io.easybreezy.hr.model.profile.Profiles
import io.easybreezy.hr.model.profile.Repository
import java.time.LocalDate

class Handler @Inject constructor(private val repository: Repository) {

    fun handleUpdatePersonalData(command: UpdatePersonalData) {
        val profile = repository.getByUser(command.id)

        profile.updatePersonalData(
            Profile.PersonalData.create(
                LocalDate.parse(command.birthday),
                Profiles.Gender.valueOf(command.gender),
                command.about
            )
        )
    }

    fun handleAddMessengers(command: AddMessengers) {
        val profile = repository.getByUser(command.id)

        // profile.updatePersonalData(
        //     Profile.PersonalData.create(
        //         LocalDate.parse(command.birthday),
        //         Profiles.Gender.valueOf(command.gender),
        //         command.about
        //     )
        // )
    }
    //
    // fun handleUpdateContactDetails(command: UpdateContactDetails) {
    //     val profile = repository.getByUser(command.id)
    //
    //     val messengers = mutableSetOf<MessengerInfo>()
    //     val phones = mutableSetOf<Phone>()
    //     command.messengers.forEach {
    //         messengers.add(MessengerInfo(Messenger.valueOf(it.), it.value))
    //     }
    //     command.phones.forEach {
    //         phones.add(Phone(it))
    //     }
    //
    //
    //     profile.updateContactDetails(
    //         Profile.ContactDetails.create(
    //             messengers,
    //             phones
    //         )
    //     )
    // }
}
