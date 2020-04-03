package io.easybreezy.hr.api.controller

import com.google.inject.Inject
import io.easybreezy.hr.application.absence.CreateAbsence
import io.easybreezy.hr.application.absence.EditWorkingHours
import io.easybreezy.hr.application.absence.Handler
import io.easybreezy.hr.application.absence.NoteWorkingHours
import io.easybreezy.hr.application.absence.RemoveWorkingHours
import io.easybreezy.hr.application.absence.UpdateAbsence
import io.easybreezy.hr.application.absence.Validation
import io.easybreezy.hr.application.absence.queryobject.Absence
import io.easybreezy.hr.application.absence.queryobject.AbsenceQO
import io.easybreezy.hr.application.absence.queryobject.Absences
import io.easybreezy.hr.application.absence.queryobject.AbsencesQO
import io.easybreezy.hr.application.absence.queryobject.UserAbsences
import io.easybreezy.hr.application.absence.queryobject.UserAbsencesQO
import io.easybreezy.hr.application.absence.queryobject.UserWorkingHours
import io.easybreezy.hr.application.absence.queryobject.UserWorkingHoursQO
import io.easybreezy.hr.application.absence.queryobject.WorkingHour
import io.easybreezy.hr.application.absence.queryobject.WorkingHourQO
import io.easybreezy.hr.application.absence.queryobject.WorkingHours
import io.easybreezy.hr.application.absence.queryobject.WorkingHoursQO
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.infrastructure.query.extractDateRange
import io.easybreezy.infrastructure.structure.Either
import java.util.UUID

class AbsenceController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val queryExecutor: QueryExecutor
) : Controller() {

    suspend fun createAbsence(command: CreateAbsence): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onCreateAbsence(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.handleCreateAbsence(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun updateAbsence(id: UUID, command: UpdateAbsence): Response.Either<Response.Ok, Response.Errors> {
        command.id = id
        val errors = validation.onUpdateAbsence(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.handleUpdateAbsence(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun removeAbsence(id: UUID): Response.Ok {
        handler.handlerRemoveAbsence(id)

        return Response.Ok
    }

    suspend fun noteWorkingHours(command: NoteWorkingHours): Response.Ok {
        validation.onNoteWorkingHours(command)
        handler.handleNoteWorkingHours(command)

        return Response.Ok
    }

    suspend fun editWorkingHours(command: EditWorkingHours): Response.Ok {
        validation.onEditWorkingHours(command)
        handler.handleEditWorkingHours(command)

        return Response.Ok
    }

    suspend fun removeWorkingHours(command: RemoveWorkingHours): Response.Ok {
        validation.onRemoveWorkingHours(command)
        handler.handleRemoveWorkingHours(command)

        return Response.Ok
    }

    suspend fun showAbsence(id: UUID): Response.Data<Absence> {
        return Response.Data(queryExecutor.execute(AbsenceQO(id)))
    }

    suspend fun myAbsences(userId: UUID): Response.Data<UserAbsences> {
        return Response.Data(
            queryExecutor.execute(UserAbsencesQO(userId))
        )
    }

    suspend fun absences(): Response.Data<Absences> {
        return Response.Data(queryExecutor.execute(AbsencesQO(call.request.extractDateRange())))
    }

    suspend fun showWorkingHour(id: UUID): Response.Data<WorkingHour> {
        return Response.Data(queryExecutor.execute(WorkingHourQO(id)))
    }

    suspend fun myWorkingHours(userId: UUID): Response.Data<UserWorkingHours> {
        return Response.Data(
            queryExecutor.execute(UserWorkingHoursQO(userId))
        )
    }

    suspend fun workingHours(): Response.Data<WorkingHours> {
        return Response.Data(queryExecutor.execute(WorkingHoursQO(call.request.extractDateRange())))
    }
}
