package io.easybreezy.user.api

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.GenericPipeline
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.ktor.Router
import io.easybreezy.infrastructure.ktor.auth.Activity
import io.easybreezy.infrastructure.ktor.auth.Auth
import io.easybreezy.infrastructure.ktor.auth.UserPrincipal
import io.easybreezy.infrastructure.ktor.auth.authorize
import io.easybreezy.integration.openapi.ktor.get
import io.easybreezy.integration.openapi.ktor.post
import io.easybreezy.integration.openapi.ktor.postParams
import io.easybreezy.user.api.controller.UserController
import io.easybreezy.user.application.Archive
import io.easybreezy.user.application.Confirm
import io.easybreezy.user.application.Create
import io.easybreezy.user.application.Invite
import io.easybreezy.user.application.UpdateActivities
import io.easybreezy.user.application.UpdateContacts
import io.easybreezy.user.application.queryobject.User
import io.ktor.application.Application
import io.ktor.auth.authenticate
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
            route("/api/users") {
                authenticate(*Auth.user) {
                    userRouting()
                }
                post<Response.Either<Response.Ok, Response.Errors>, Confirm>("/confirm") { command ->
                    controller<UserController>(this).confirm(command)
                }
            }
        }
    }

    private fun Route.userRouting() {
        authorize(setOf(Activity.USERS_MANAGE)) {
            get<Response.Listing<User>>("") { controller<UserController>(this).users() }
            post<Response.Either<Response.Ok, Response.Errors>, Invite>("/invite") { invite ->
                controller<UserController>(this).invite(invite)
            }
            postParams<Response.Ok, ID>("/{id}/hire") { params ->
                controller<UserController>(this).hire(params.id)
            }
            post<Response.Either<Response.Ok, Response.Errors>, Archive, ID>("/{id}/archive") { command, params ->
                controller<UserController>(this).archive(params.id, command)
            }
            post<Response.Either<Response.Ok, Response.Errors>, Create>("") { command ->
                controller<UserController>(this).create(command)
            }
            post<Response.Either<Response.Ok, Response.Errors>, UpdateActivities, ID>("/{id}/activities") { command, params ->
                controller<UserController>(this).updateActivities(params.id, command)
            }
            post<Response.Either<Response.Ok, Response.Errors>, UpdateContacts, ID>("/update-contacts/{id}") { command, params ->
                controller<UserController>(this).updateContacts(command, params.id)
            }
        }

        post<Response.Either<Response.Ok, Response.Errors>, UpdateContacts>("/update-contacts") { command ->
            controller<UserController>(this).updateContacts(command, resolvePrincipal<UserPrincipal>())
        }
        get<Response.Data<User>>("/me") { controller<UserController>(this).me(resolvePrincipal<UserPrincipal>()) }
    }

    internal data class ID(val id: UUID)
}
