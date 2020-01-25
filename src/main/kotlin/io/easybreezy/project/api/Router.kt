package io.easybreezy.project.api

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.GenericPipeline
import io.easybreezy.infrastructure.ktor.Router
import io.easybreezy.project.api.controller.ProjectController
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.routing.*
import java.util.*

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
            post("") { controller<ProjectController>(this).create(call.receive()) }
            get("/{id}") { controller<ProjectController>(this).show(UUID.fromString(call.parameters["id"])) }
            post("/{id}/add/role") {
                controller<ProjectController>(this).addRole(
                    UUID.fromString(call.parameters["id"]),
                    call.receive()
                )
            }
            get("/{id}/remove/role/{role_id}") {
                controller<ProjectController>(this).removeRole(
                    UUID.fromString(call.parameters["id"]),
                    UUID.fromString(call.parameters["role_id"])
                )
            }
        }
    }
}
