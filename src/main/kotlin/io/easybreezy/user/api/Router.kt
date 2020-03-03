package io.easybreezy.user.api

import com.google.inject.Inject
import io.easybreezy.infrastructure.structure.Either
import io.easybreezy.infrastructure.ktor.GenericPipeline
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.ktor.Router
import io.easybreezy.infrastructure.ktor.auth.Auth
import io.easybreezy.infrastructure.ktor.auth.UserPrincipal
import io.easybreezy.infrastructure.ktor.get
import io.easybreezy.infrastructure.ktor.post
import io.easybreezy.user.api.controller.UserController
import io.easybreezy.user.application.Confirm
import io.easybreezy.user.application.Invite
import io.easybreezy.user.application.UpdateContacts
import io.easybreezy.user.application.queryobject.User
import io.ktor.application.Application
import io.ktor.auth.authenticate
import io.ktor.routing.Route
import io.ktor.routing.route
import io.ktor.routing.routing

class Router @Inject constructor(
    application: Application,
    genericPipeline: GenericPipeline
) : Router(application, genericPipeline) {

    init {
        application.routing {
            route("/api/users") {
                authenticate(*Auth.user) {
                    userRouting(this)
                }
                post<Response.Ok, Confirm>("/confirm") { command -> controller<UserController>(this).confirm(command) }
            }
        }
    }

    private fun userRouting(route: Route) {
        route.route("") {
            get<Response.Listing<User>>("") { controller<UserController>(this).index() }
            get<Response.Data<User>>("/me") { controller<UserController>(this).me(resolvePrincipal<UserPrincipal>()) }
            post<Response.Either<Response.Ok, Response.Errors>, Invite>("/invite") { invite ->
                controller<UserController>(this).invite(invite)
            }

            post<Response.Either<Response.Ok, Response.Errors>, UpdateContacts>("/update-contacts") { command ->
                controller<UserController>(this).updateContacts(command, resolvePrincipal<UserPrincipal>())
            }
        }
    }
}
