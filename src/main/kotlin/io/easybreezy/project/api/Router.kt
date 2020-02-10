package io.easybreezy.project.api

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.GenericPipeline
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.ktor.Router
import io.easybreezy.infrastructure.ktor.get
import io.easybreezy.infrastructure.ktor.post
import io.easybreezy.project.api.controller.ProjectController
import io.easybreezy.project.application.project.command.New
import io.easybreezy.project.application.project.command.NewRole
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.route
import io.ktor.routing.routing
import java.util.UUID

class Router @Inject constructor(
    application: Application,
    genericPipeline: GenericPipeline
) : Router(application, genericPipeline) {

    init {
        application.routing {
            route("/api") {
                projectRoutes(this)
            }
        }
    }

    private fun projectRoutes(route: Route) {
        route.route("/projects") {
            data class QueryParams(val id: UUID, val roleId: UUID)
            post<Response.Ok, New>("") { new -> controller<ProjectController>(this).create(new) }
            post<Response.Ok, NewRole, UUID>("/{id}/add/role") { command, uuid ->
                controller<ProjectController>(this).addRole(
                    uuid,
                    command
                )
            }
            get<Response.Ok, QueryParams>("/{id}/remove/role/{role_id}") { params ->
                controller<ProjectController>(this).removeRole(params.id, params.roleId)
            }
        }
    }
}
