package io.easybreezy.hr.api.controller

import com.google.inject.Inject
import io.easybreezy.hr.application.absence.CreateAbsence
import io.easybreezy.hr.application.absence.EditWorkingHours
import io.easybreezy.hr.application.absence.Handler
import io.easybreezy.hr.application.absence.NoteWorkingHours
import io.easybreezy.hr.application.absence.RemoveWorkingHours
import io.easybreezy.hr.application.absence.UpdateAbsence
import io.easybreezy.hr.application.absence.Validation
import io.easybreezy.hr.application.absence.queryobject.AbsenceQO
import io.easybreezy.hr.application.absence.queryobject.AbsencesQO
import io.easybreezy.hr.application.absence.queryobject.WorkingHourQO
import io.easybreezy.hr.application.absence.queryobject.WorkingHoursQO
import io.easybreezy.hr.infrastructure.AbsenceRepository
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.respondListing
import io.easybreezy.infrastructure.ktor.respondOk
import io.easybreezy.infrastructure.ktor.respondWith
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.infrastructure.query.pagingParameters
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class AbsenceController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val repository: AbsenceRepository,
    private val queryExecutor: QueryExecutor
) : Controller() {

    suspend fun createAbsence(command: CreateAbsence) {
        validation.onCreateAbsence(command)
        transaction {
            handler.handleCreateAbsence(command)
        }

        call.respondOk()
    }

    suspend fun updateAbsence(id: UUID, command: UpdateAbsence) {
        command.id = id
        validation.onUpdateAbsence(command)
        transaction {
            handler.handleUpdateAbsence(command)
        }

        call.respondOk()
    }

    suspend fun removeAbsence(id: UUID) {
        repository.remove(id)

        call.respondOk()
    }

    suspend fun noteWorkingHours(command: NoteWorkingHours) {
        validation.onNoteWorkingHours(command)
        transaction {
            handler.handleNoteWorkingHours(command)
        }

        call.respondOk()
    }

    suspend fun editWorkingHours(command: EditWorkingHours) {
        validation.onEditWorkingHours(command)
        transaction {
            handler.handleEditWorkingHours(command)
        }

        call.respondOk()
    }

    suspend fun removeWorkingHours(command: RemoveWorkingHours) {
        validation.onRemoveWorkingHours(command)
        transaction {
            handler.handleRemoveWorkingHours(command)
        }

        call.respondOk()
    }

    suspend fun showAbsence(id: UUID) {
        call.respondWith {
            data = queryExecutor.execute(AbsenceQO(id))
        }
    }

    suspend fun absences(userId: UUID) {
        call.respondListing(
            queryExecutor.execute(AbsencesQO(userId, call.request.pagingParameters()))
        )
    }

    suspend fun showWorkingHour(id: UUID) {
        call.respondWith {
            data = queryExecutor.execute(WorkingHourQO(id))
        }
    }

    suspend fun workingHours(userId: UUID) {
        call.respondListing(
            queryExecutor.execute(WorkingHoursQO(userId, call.request.pagingParameters()))
        )
    }
}