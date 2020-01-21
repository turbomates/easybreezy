package io.easybreezy.hr.api.controller

import com.google.inject.Inject
import io.easybreezy.hr.application.absence.Create
import io.easybreezy.hr.application.absence.Handler
import io.easybreezy.hr.application.absence.Update
import io.easybreezy.hr.application.absence.Validation
import io.easybreezy.hr.model.absence.AbsenceId
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.respondOk
import org.jetbrains.exposed.sql.transactions.transaction

class AbsenceController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation
) : Controller() {

    suspend fun create(command: Create) {
        validation.onCreate(command)
        transaction {
            handler.create(command)
        }

        call.respondOk()
    }

    suspend fun update(command: Update) {
        TODO("Not implemented")
    }

    suspend fun show(id: AbsenceId) {
        TODO("Not implemented")
    }

    suspend fun absences() {
        TODO("Not implemented")
    }
}