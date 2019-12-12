package io.easybreezy.calendar.api

import com.google.inject.Inject
import io.easybreezy.calendar.api.controller.AbsenceController
import io.easybreezy.calendar.model.AbsenceId
import io.easybreezy.infrastructure.ktor.GenericPipeline
import io.easybreezy.infrastructure.ktor.Router
import io.easybreezy.infrastructure.ktor.auth.Auth
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.locations.Location
import io.ktor.request.receive
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.locations.get
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
                authenticate(*Auth.user) {
                    userRouting(this)
                }
            }
        }
    }

    private fun userRouting(route: Route) {
        route.route("/absences") {
            @Location("/{id}")
            data class Show(val id: AbsenceId)

            get<Show> { controller<AbsenceController>(this).show(it.id) }
            get("") { controller<AbsenceController>(this).absences() }
            post("/create") { controller<AbsenceController>(this).create(call.receive()) }
            post("/update") { controller<AbsenceController>(this).update(call.receive()) }
        }
    }
}