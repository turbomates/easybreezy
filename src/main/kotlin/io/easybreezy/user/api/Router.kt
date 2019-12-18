package io.easybreezy.user.api

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.GenericPipeline
import io.easybreezy.infrastructure.ktor.Router
import io.easybreezy.infrastructure.ktor.auth.Auth
import io.easybreezy.user.api.controller.UserController
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing

class Router @Inject constructor(
    application: Application,
    genericPipeline: GenericPipeline
) : Router(application, genericPipeline) {

    init {
        application.routing {
            route("/api") {
                // authenticate(*Auth.user) {
                    userRouting(this)
                // }
            }
        }
    }

    private fun userRouting(route: Route) {
        route.route("/users") {
            get("") { controller<UserController>(this).index() }

            post("/invite") { controller<UserController>(this).invite(call.receive()) }
            post("/confirm") { controller<UserController>(this).confirm(call.receive()) }
        }
    }
}
