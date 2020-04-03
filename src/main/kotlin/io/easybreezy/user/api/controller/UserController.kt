package io.easybreezy.user.api.controller

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.infrastructure.query.pagingParameters
import io.easybreezy.infrastructure.structure.Either
import io.easybreezy.user.application.Confirm
import io.easybreezy.user.application.Handler
import io.easybreezy.user.application.Invite
import io.easybreezy.user.application.UpdateContacts
import io.easybreezy.user.application.Validation
import io.easybreezy.user.application.queryobject.User
import io.easybreezy.user.application.queryobject.UserQO
import io.easybreezy.user.application.queryobject.UsersQO
import java.util.UUID

class UserController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val queryExecutor: QueryExecutor
) : Controller() {

    suspend fun index(): Response.Listing<User> {
        return Response.Listing(
            queryExecutor.execute(UsersQO(call.request.pagingParameters()))
        )
    }

    suspend fun me(id: UUID): Response.Data<User> {
        return Response.Data(queryExecutor.execute(UserQO(id)))
    }

    suspend fun invite(command: Invite): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onInvite(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.handleInvite(command)

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

    suspend fun confirm(command: Confirm): Response.Ok {
        handler.handleConfirm(command)

        return Response.Ok
    }
}
