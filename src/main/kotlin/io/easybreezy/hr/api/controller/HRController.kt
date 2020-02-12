package io.easybreezy.hr.api.controller

import com.google.inject.Inject
import io.easybreezy.hr.application.hr.command.*
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.structure.Either
import java.util.*

class HRController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation
) : Controller() {

    suspend fun createCard(command: CreateCard, userId: UUID): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onCreateCard(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.createCard(command, userId)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun hire(command: Hire, employee: UUID, hrManager: UUID): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onHire(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.hire(command, employee, hrManager)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun fire(command: Fire, employee: UUID, hrManager: UUID): Response.Either<Response.Ok, Response.Errors> {

        val errors = validation.onFire(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.fire(command, employee, hrManager)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun writeNote(command: WriteNote, employee: UUID, hrManager: UUID): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onWriteNote(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.writeNote(command, employee, hrManager)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun applyPosition(command: ApplyPosition, employee: UUID, hrManager: UUID): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onApplyPosition(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.applyPosition(command, employee, hrManager)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun applySalary(command: ApplySalary, employee: UUID, hrManager: UUID): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onApplySalary(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.applySalary(command, employee, hrManager)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun specifySkills(command: SpecifySkills, employee: UUID): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onSpecifySkills(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.specifySkills(command, employee)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun updateBio(command: UpdateBio, employee: UUID): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onUpdateBio(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.updateBio(command, employee)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun updateBirthday(command: UpdateBirthday, employee: UUID): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onUpdateBirthday(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.updateBirthday(command, employee)
        return Response.Either(Either.Left(Response.Ok))
    }
}
