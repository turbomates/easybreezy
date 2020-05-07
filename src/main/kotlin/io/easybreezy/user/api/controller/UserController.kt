package io.easybreezy.user.api.controller

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.infrastructure.query.pagingParameters
import io.easybreezy.infrastructure.structure.Either
import io.easybreezy.user.application.command.Archive
import io.easybreezy.user.application.command.Confirm
import io.easybreezy.user.application.command.Create
import io.easybreezy.user.application.command.Handler
import io.easybreezy.user.application.command.Invite
import io.easybreezy.user.application.command.UpdateActivities
import io.easybreezy.user.application.command.UpdateContacts
import io.easybreezy.user.application.command.Validation
import io.easybreezy.user.application.queryobject.User
import io.easybreezy.user.application.queryobject.UserQO
import io.easybreezy.user.application.queryobject.UsersQO
import java.util.UUID

class UserController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val queryExecutor: QueryExecutor
) : Controller() {

    suspend fun users(): Response.Listing<User> {
        return Response.Listing(
            queryExecutor.execute(UsersQO(call.request.pagingParameters()))
        )
    }

    suspend fun me(id: UUID): Response.Data<User> {
        return Response.Data(queryExecutor.execute(UserQO(id)))
    }

    suspend fun create(command: Create): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onCreate(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.handleCreate(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun invite(command: Invite): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onInvite(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.handleInvite(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun hire(userId: UUID): Response.Ok {
        handler.handleHire(userId)

        return Response.Ok
    }

    suspend fun archive(userId: UUID, command: Archive): Response.Either<Response.Ok, Response.Errors> {
        command.userId = userId
        val errors = validation.onArchive(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.handleArchive(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun updateContacts(command: UpdateContacts, id: UUID): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onUpdateContacts(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.handleUpdateContacts(command, id)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun updateActivities(userId: UUID, command: UpdateActivities): Response.Either<Response.Ok, Response.Errors> {
        command.userId = userId
        val errors = validation.onUpdateActivities(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }

        handler.handleUpdateActivities(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun confirm(command: Confirm): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onConfirm(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.handleConfirm(command)

        return Response.Either(Either.Left(Response.Ok))
    }
}
