package io.easybreezy.hr.api.controller

import com.google.inject.Inject
import io.easybreezy.hr.application.event.command.AddParticipants
import io.easybreezy.hr.application.event.command.ChangeConditions
import io.easybreezy.hr.application.event.command.ChangeVisitStatus
import io.easybreezy.hr.application.event.command.Enter
import io.easybreezy.hr.application.event.command.Handler
import io.easybreezy.hr.application.event.command.Open
import io.easybreezy.hr.application.event.command.UpdateDetails
import io.easybreezy.hr.application.event.command.Validation
import io.easybreezy.hr.application.event.queryobject.Event
import io.easybreezy.hr.application.event.queryobject.EventQO
import io.easybreezy.hr.application.event.queryobject.EventsQO
import io.easybreezy.hr.model.event.EventId
import io.easybreezy.hr.model.event.Owner
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.infrastructure.query.pagingParameters
import io.easybreezy.infrastructure.structure.Either
import java.util.UUID

class EventController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val queryExecutor: QueryExecutor
) : Controller() {
    suspend fun openEvent(owner: Owner, command: Open): Response.Either<Response.Ok, Response.Errors> {
        command.owner = owner
        val errors = validation.onCreateEvent(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.openEvent(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun updateEventDetails(
        event: EventId,
        command: UpdateDetails
    ): Response.Either<Response.Ok, Response.Errors> {
        command.id = event
        val errors = validation.onUpdateEvent(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.updateEventDetails(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun updateEventConditions(
        event: EventId,
        command: ChangeConditions
    ): Response.Either<Response.Ok, Response.Errors> {
        command.event = event
        val errors = validation.onChangeConditions(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.changeEventConditions(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun addParticipants(
        event: EventId,
        command: AddParticipants
    ): Response.Either<Response.Ok, Response.Errors> {
        command.event = event
        val errors = validation.onAddParticipants(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.addParticipants(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun enterEvent(event: EventId, command: Enter): Response.Either<Response.Ok, Response.Errors> {
        command.event = event
        val errors = validation.onEnterEvent(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.enterEvent(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun changeVisitStatus(event: EventId, command: ChangeVisitStatus): Response.Either<Response.Ok, Response.Errors> {
        command.event = event
        val errors = validation.onChangeVisitStatus(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.changeParticipantVisitStatus(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun cancelEvent(event: EventId): Response.Ok {
        handler.cancelEvent(event)

        return Response.Ok
    }

    suspend fun event(event: EventId): Response.Data<Event> {
        return Response.Data(queryExecutor.execute(EventQO(event)))
    }

    suspend fun events(viewer: UUID): Response.Listing<Event> {
        return Response.Listing(
            queryExecutor.execute(EventsQO(viewer, call.request.pagingParameters()))
        )
    }
}
