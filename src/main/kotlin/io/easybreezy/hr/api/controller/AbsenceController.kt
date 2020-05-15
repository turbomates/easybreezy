package io.easybreezy.hr.api.controller

import com.google.inject.Inject
import io.easybreezy.hr.application.absence.CreateAbsence
import io.easybreezy.hr.application.absence.Handler
import io.easybreezy.hr.application.absence.UpdateAbsence
import io.easybreezy.hr.application.absence.Validation
import io.easybreezy.hr.application.absence.queryobject.Absence
import io.easybreezy.hr.application.absence.queryobject.AbsenceQO
import io.easybreezy.hr.application.absence.queryobject.Absences
import io.easybreezy.hr.application.absence.queryobject.AbsencesQO
import io.easybreezy.hr.application.absence.queryobject.UserAbsences
import io.easybreezy.hr.application.absence.queryobject.UserAbsencesQO
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

    suspend fun approveAbsence(id: UUID): Response.Ok {
        handler.handleApproveAbsence(id)

        return Response.Ok
    }

    suspend fun removeAbsence(id: UUID): Response.Ok {
        handler.handlerRemoveAbsence(id)

        return Response.Ok
    }

    suspend fun showAbsence(id: UUID): Response.Data<Absence> {
        return Response.Data(queryExecutor.execute(AbsenceQO(id)))
    }

    suspend fun userAbsences(userId: UUID): Response.Data<UserAbsences> {
        return Response.Data(
            queryExecutor.execute(UserAbsencesQO(userId))
        )
    }

    suspend fun absences(): Response.Data<Absences> {
        return Response.Data(queryExecutor.execute(AbsencesQO(call.request.extractDateRange())))
    }
}
