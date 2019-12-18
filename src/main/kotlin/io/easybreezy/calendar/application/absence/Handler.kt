package io.easybreezy.calendar.application.absence

import com.google.inject.Inject
import io.easybreezy.calendar.infrastructure.AbsenceRepository
import io.easybreezy.calendar.model.Absence
import io.easybreezy.calendar.model.Reason

class Handler @Inject constructor(private val repository: AbsenceRepository) {
    fun create(command: Create) {
        val absence = Absence(
            command.startedAt,
            command.endedAt,
            command.comment,
            Reason.valueOf(command.reason.toUpperCase()),
            command.userId
        )
        repository.add(absence)
    }

    fun update(command: Update) {
        TODO("Not implemented")
    }
}