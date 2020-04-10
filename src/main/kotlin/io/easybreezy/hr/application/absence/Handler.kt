package io.easybreezy.hr.application.absence

import com.google.inject.Inject
import io.easybreezy.hr.infrastructure.AbsenceRepository
import io.easybreezy.hr.infrastructure.WorkingHourRepository
import io.easybreezy.hr.model.absence.Absence
import io.easybreezy.hr.model.absence.Reason
import io.easybreezy.hr.model.absence.WorkingHour
import io.easybreezy.infrastructure.exposed.TransactionManager
import java.util.UUID

class Handler @Inject constructor(
    private val absenceRepository: AbsenceRepository,
    private val workingHourRepository: WorkingHourRepository,
    private val transaction: TransactionManager
) {

    suspend fun handleCreateAbsence(command: CreateAbsence) {
        transaction {
            val absence = Absence.create(
                command.startedAt,
                command.endedAt,
                Reason.valueOf(command.reason),
                command.userId
            )
            absence.comment = command.comment
        }
    }

    suspend fun handleUpdateAbsence(command: UpdateAbsence) {
        transaction {
            val absence = absenceRepository.getOne(command.id)
            absence.edit(
                command.startedAt,
                command.endedAt,
                Reason.valueOf(command.reason)
            )
            absence.comment = command.comment
        }
    }

    suspend fun handleNoteWorkingHours(command: NoteWorkingHours) {
        transaction {
            command.workingHours.forEach {
                WorkingHour.create(
                    it.day,
                    it.count,
                    command.userId
                )
            }
        }
    }

    suspend fun handleEditWorkingHours(command: EditWorkingHours) {
        transaction {
            command.workingHours.forEach { (id, dto) ->
                val workingHour = workingHourRepository.getOne(id)
                workingHour.edit(dto.day, dto.count)
            }
        }
    }

    suspend fun handlerRemoveAbsence(id: UUID) {
        transaction {
            absenceRepository.remove(id)
        }
    }
}
