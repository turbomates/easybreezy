package io.easybreezy.hr.api.controller

import com.google.inject.Inject
import io.easybreezy.hr.application.hr.command.*
import io.easybreezy.hr.application.hr.queryobject.Employee
import io.easybreezy.hr.application.hr.queryobject.EmployeeDetails
import io.easybreezy.hr.application.hr.queryobject.EmployeeDetailsQO
import io.easybreezy.hr.application.hr.queryobject.EmployeesQO
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.infrastructure.query.pagingParameters
import io.easybreezy.infrastructure.structure.Either
import java.util.*

class HRController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val queryExecutor: QueryExecutor
) : Controller() {

    suspend fun createCard(command: CreateCard, userId: UUID): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onCreateCard(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.createCard(command, userId)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun hire(command: Hire, employeeUser: UUID, hrManager: UUID): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onHire(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.hire(command, employeeUser, hrManager)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun fire(command: Fire, employeeUser: UUID, hrManager: UUID): Response.Either<Response.Ok, Response.Errors> {

        val errors = validation.onFire(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.fire(command, employeeUser, hrManager)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun writeNote(command: WriteNote, employeeUser: UUID, hrManager: UUID): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onWriteNote(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.writeNote(command, employeeUser, hrManager)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun applyPosition(command: ApplyPosition, employeeUser: UUID, hrManager: UUID): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onApplyPosition(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.applyPosition(command, employeeUser, hrManager)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun applySalary(command: ApplySalary, employeeUser: UUID, hrManager: UUID): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onApplySalary(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.applySalary(command, employeeUser, hrManager)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun specifySkills(command: SpecifySkills, employeeUser: UUID): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onSpecifySkills(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.specifySkills(command, employeeUser)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun updateBio(command: UpdateBio, employeeUser: UUID): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onUpdateBio(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.updateBio(command, employeeUser)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun updateBirthday(command: UpdateBirthday, employeeUser: UUID): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onUpdateBirthday(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.updateBirthday(command, employeeUser)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun employees(): Response.Listing<Employee> {
         return Response.Listing(
            queryExecutor.execute(EmployeesQO(call.request.pagingParameters()))
        )
    }

    suspend fun employee(employeeUser: UUID): Response.Data<EmployeeDetails> {
        return Response.Data(
            queryExecutor.execute(EmployeeDetailsQO(employeeUser))
        )
    }
}
