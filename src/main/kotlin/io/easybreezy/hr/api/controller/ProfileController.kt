package io.easybreezy.hr.api.controller

import com.google.inject.Inject
import io.easybreezy.hr.application.profile.command.UpdateMessengers
import io.easybreezy.hr.application.profile.command.Handler
import io.easybreezy.hr.application.profile.command.UpdateContactDetails
import io.easybreezy.hr.application.profile.command.UpdatePersonalData
import io.easybreezy.hr.application.profile.command.Validation
import io.easybreezy.hr.application.profile.queryobject.ProfileQO
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.respondData
import io.easybreezy.infrastructure.ktor.respondOk
import io.easybreezy.infrastructure.query.QueryExecutor
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ProfileController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val queryExecutor: QueryExecutor
) : Controller() {

    suspend fun show(id: UUID) {
        call.respondData(queryExecutor.execute(ProfileQO(id)))
    }

    suspend fun updatePersonalData(id: UUID, command: UpdatePersonalData) {
        command.id = id
        validation.onUpdatePersonalData(command)
        transaction {
            handler.handleUpdatePersonalData(command)
        }

        call.respondOk()
    }

    suspend fun updateContactDetails(id: UUID, command: UpdateContactDetails) {
        command.id = id
        transaction {
            handler.handleUpdateContactDetails(command)
        }

        call.respondOk()
    }

    suspend fun updateMessengers(id: UUID, command: UpdateMessengers) {
        command.id = id
        transaction {
            handler.handleUpdateMessengers(command)
        }

        call.respondOk()
    }
}
