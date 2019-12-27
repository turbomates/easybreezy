package io.easybreezy.user.api.controller

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.respondOk
import io.easybreezy.user.application.Confirm
import io.easybreezy.user.application.Handler
import io.easybreezy.user.application.Invite
import io.easybreezy.user.application.Validation
import io.ktor.response.respondText
import org.jetbrains.exposed.sql.transactions.transaction

class UserController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation
) : Controller() {

    suspend fun index() {
        call.respondText("USERS")
    }

    suspend fun invite(command: Invite) {
        validation.onInvite(command)
        transaction {
            handler.handleInvite(command)
        }

        call.respondOk()
    }

    suspend fun confirm(command: Confirm) {
        transaction {
            handler.handleConfirm(command)
        }

        call.respondOk()
    }
}
