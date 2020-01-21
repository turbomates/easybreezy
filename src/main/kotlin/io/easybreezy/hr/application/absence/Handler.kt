package io.easybreezy.hr.application.absence

import com.google.inject.Inject
import io.easybreezy.hr.infrastructure.AbsenceRepository
import io.easybreezy.hr.model.absence.Absence
import io.easybreezy.hr.model.absence.Reason
import io.easybreezy.user.infrastructure.UserRepository

class Handler @Inject constructor(private val repository: AbsenceRepository, private val userRepository: UserRepository) {
    fun create(command: Create) {
        Absence.create(
            command.startedAt,
            command.endedAt,
            Reason.valueOf(command.reason),
            userRepository.getOne(command.userId),
            command.comment
        )
    }

    fun update(command: Update) {
        TODO("Not implemented")
    }
}