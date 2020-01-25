package io.easybreezy.hr.application.absence

import com.google.inject.Inject
import io.easybreezy.hr.infrastructure.AbsenceRepository
import io.easybreezy.hr.infrastructure.WorkingHourRepository
import io.easybreezy.hr.model.absence.Absence
import io.easybreezy.hr.model.absence.Reason
import io.easybreezy.hr.model.absence.WorkingHour
import io.easybreezy.user.infrastructure.UserRepository

class Handler @Inject constructor(
    private val absenceRepository: AbsenceRepository,
    private val workingHourRepository: WorkingHourRepository,
    private val userRepository: UserRepository
) {

    fun handleCreateAbsence(command: CreateAbsence) {
        Absence.create(
            command.startedAt,
            command.endedAt,
            Reason.valueOf(command.reason),
            userRepository.getOne(command.userId),
            command.comment
        )
    }

    fun handleUpdateAbsence(command: UpdateAbsence) {
        val absence = absenceRepository.getOne(command.id)
        absence.edit(
            command.startedAt,
            command.endedAt,
            Reason.valueOf(command.reason),
            command.comment
        )
    }

    fun handleNoteWorkingHours(command: NoteWorkingHours) {
        command.workingHours.forEach {
            WorkingHour.create(
                it.day,
                it.count,
                userRepository.getOne(command.userId)
            )
        }

    }

    fun handleEditWorkingHours(command: EditWorkingHours) {
        command.workingHours.forEach { (id, dto) ->
            val workingHour = workingHourRepository.getOne(id)
            workingHour.edit(dto.day, dto.count)
        }
    }

    fun handleRemoveWorkingHours(command: RemoveWorkingHours) {
        workingHourRepository.remove(command.workingHours)
    }
}