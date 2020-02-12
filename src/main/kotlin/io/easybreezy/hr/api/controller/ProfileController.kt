package io.easybreezy.hr.api.controller

import com.google.inject.Inject
import io.easybreezy.hr.application.profile.command.Handler
import io.easybreezy.hr.application.profile.command.UpdateContactDetails
import io.easybreezy.hr.application.profile.command.UpdateMessengers
import io.easybreezy.hr.application.profile.command.Validation
import io.easybreezy.hr.application.profile.queryobject.Profile
import io.easybreezy.hr.application.profile.queryobject.ProfileQO
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.query.QueryExecutor
import java.util.UUID

class ProfileController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val queryExecutor: QueryExecutor
) : Controller() {

    suspend fun show(id: UUID): Response.Data<Profile> {
        return Response.Data(queryExecutor.execute(ProfileQO(id)))
    }

    suspend fun updateContactDetails(id: UUID, command: UpdateContactDetails): Response.Ok {
        command.id = id
        handler.handleUpdateContactDetails(command)
        return Response.Ok
    }

    suspend fun updateMessengers(id: UUID, command: UpdateMessengers): Response.Ok {
        command.id = id
        handler.handleUpdateMessengers(command)
        return Response.Ok
    }
}
