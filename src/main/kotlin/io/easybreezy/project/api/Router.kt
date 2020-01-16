package io.easybreezy.project.api

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.GenericPipeline
import io.easybreezy.infrastructure.ktor.Router
import io.easybreezy.project.api.controller.ProjectController
import io.ktor.application.Application
import io.ktor.routing.*
import kotlinx.serialization.ImplicitReflectionSerializer

class Router @Inject constructor(
    application: Application,
    genericPipeline: GenericPipeline
) : Router(application, genericPipeline) {

    init {
        application.routing {
            route("/api") {
                userRouting(this)
            }
        }
    }

    private fun userRouting(route: Route) {
        route.route("/projects") {
            get("") { controller<ProjectController>(this).create() }

        }
    }
}
