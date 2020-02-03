package io.easybreezy.user.api.controller

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.respondListing
import io.easybreezy.infrastructure.ktor.respondOk
import io.easybreezy.infrastructure.ktor.respondWith
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.infrastructure.query.pagingParameters
import io.easybreezy.user.application.Confirm
import io.easybreezy.user.application.Handler
import io.easybreezy.user.application.Invite
import io.easybreezy.user.application.Validation
import io.easybreezy.user.application.queryobject.UserQO
import io.easybreezy.user.application.queryobject.UsersQO
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

data class ListTest<T>(
    val list: List<T>,
    val test: String = "asd"
)

class UserController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val queryExecutor: QueryExecutor
) : Controller() {

    suspend fun index() {
        call.respondListing(
            queryExecutor.execute(UsersQO(call.request.pagingParameters()))
        )
    }

    suspend fun me(id: UUID) {
        call.respondWith {
            data = queryExecutor.execute(UserQO(id))
        }
    }

    suspend fun invite(command: Invite) {
        validation.onInvite(command)
        handler.handleInvite(command)

        call.respondOk()
    }

    suspend fun confirm(command: Confirm) {
        transaction {
            handler.handleConfirm(command)
        }

        call.respondOk()
    }
}
