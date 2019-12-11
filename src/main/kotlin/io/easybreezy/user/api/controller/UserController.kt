package io.easybreezy.user.api.controller

import com.google.inject.Inject
import io.easybreezy.infrastructure.hibernate.TransactionWrapper
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.respondOk
import io.easybreezy.user.application.Confirm
import io.easybreezy.user.application.Handler
import io.easybreezy.user.application.Invite
import io.easybreezy.user.application.Validation
import io.ktor.response.respondText

class UserController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val transactionWrapper: TransactionWrapper
) : Controller() {

    suspend fun index() {
        call.respondText("USERS")
    }

    suspend fun invite(command: Invite) {
        validation.onInvite(command)
        transactionWrapper { handler.handleInvite(command) }

        call.respondOk()
    }

    suspend fun confirm(command: Confirm) {
        transactionWrapper { handler.handleConfirm(command) }

        call.respondOk()
    }
}
