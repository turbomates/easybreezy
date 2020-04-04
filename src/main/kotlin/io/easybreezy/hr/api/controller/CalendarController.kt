package io.easybreezy.hr.api.controller

import com.google.inject.Inject
import io.easybreezy.hr.application.calendar.command.AddHoliday
import io.easybreezy.hr.application.calendar.command.EditCalendar
import io.easybreezy.hr.application.calendar.command.EditHoliday
import io.easybreezy.hr.application.calendar.command.Handler
import io.easybreezy.hr.application.calendar.command.ImportCalendar
import io.easybreezy.hr.application.calendar.command.RemoveHoliday
import io.easybreezy.hr.application.calendar.command.Validation
import io.easybreezy.hr.application.calendar.queryobject.Calendars
import io.easybreezy.hr.application.calendar.queryobject.CalendarsQO
import io.easybreezy.hr.application.calendar.queryobject.Holidays
import io.easybreezy.hr.application.calendar.queryobject.HolidaysQO
import io.easybreezy.hr.infrastructure.CalendarRepository
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.infrastructure.structure.Either
import java.util.UUID

class CalendarController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val queryExecutor: QueryExecutor,
    private val calendarRepository: CalendarRepository
) : Controller() {

    suspend fun importCalendar(command: ImportCalendar): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onImportCalendar(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }

        handler.importCalendar(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun editCalendar(id: UUID, command: EditCalendar): Response.Either<Response.Ok, Response.Errors> {
        command.id = id
        val errors = validation.onEditCalendar(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }

        handler.editCalendar(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun removeCalendar(id: UUID): Response.Ok {
        handler.removeCalendar(id)

        return Response.Ok
    }

    suspend fun calendars(): Response.Data<Calendars> {
        return Response.Data(queryExecutor.execute(CalendarsQO()))
    }

    suspend fun addHoliday(command: AddHoliday): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onAddHoliday(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }

        handler.addHoliday(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun editHoliday(calendarId: UUID, command: EditHoliday): Response.Either<Response.Ok, Response.Errors> {
        command.calendarId = calendarId
        val errors = validation.onEditHoliday(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }

        handler.editHoliday(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun removeHoliday(command: RemoveHoliday): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onRemoveHoliday(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }

        handler.removeHoliday(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun holidays(): Response.Data<Holidays> {
        return Response.Data(queryExecutor.execute(HolidaysQO()))
    }
}
