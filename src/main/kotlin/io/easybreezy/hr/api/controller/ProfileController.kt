package io.easybreezy.hr.api.controller

import com.google.inject.Inject
import io.easybreezy.hr.application.profile.Handler
import io.easybreezy.hr.application.profile.UpdateContactDetails
import io.easybreezy.hr.application.profile.UpdatePersonalData
import io.easybreezy.hr.application.profile.Validation
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.respondOk
import io.easybreezy.infrastructure.query.QueryExecutor
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ProfileController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val queryExecutor: QueryExecutor
) : Controller() {

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
        // validation.onUpdateContactDetails(command)
        transaction {
            handler.handleUpdateContactDetails(command)
        }

        call.respondOk()
    }

}
