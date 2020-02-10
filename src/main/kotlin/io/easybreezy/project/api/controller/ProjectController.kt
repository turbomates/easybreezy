package io.easybreezy.project.api.controller

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.project.application.project.command.Handler
import io.easybreezy.project.application.project.command.New
import io.easybreezy.project.application.project.command.NewRole
import java.util.UUID

class ProjectController @Inject constructor(private val handler: Handler) : Controller() {
    suspend fun create(new: New): Response.Ok {
        handler.new(new)
        return Response.Ok
    }

    suspend fun show(id: UUID) {
    }

    suspend fun removeRole(id: UUID, roleId: UUID): Response.Ok {
        return Response.Ok
    }

    suspend fun addRole(id: UUID, role: NewRole): Response.Ok {
        role.projectID = id
        handler.addRole(role)
        return Response.Ok
    }
}
