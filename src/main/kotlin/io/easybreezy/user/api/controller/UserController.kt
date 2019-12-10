package io.easybreezy.user.api.controller

import com.google.inject.Inject
import io.easybreezy.infrastructure.hibernate.TransactionWrapper
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.respondOk
import io.easybreezy.user.application.Handler
import io.easybreezy.user.application.InviteUser
import io.ktor.response.respondText

class UserController @Inject constructor(
    private val handler: Handler,
    private val transactionWrapper: TransactionWrapper
) : Controller() {

    suspend fun index() {
        call.respondText("USERS")
    }

    suspend fun invite(command: InviteUser) {
        transactionWrapper { handler.handleInvite(command) }

        call.respondOk()
    }
}
