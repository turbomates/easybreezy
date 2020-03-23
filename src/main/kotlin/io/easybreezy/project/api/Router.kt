package io.easybreezy.project.api

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.GenericPipeline
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.ktor.Router
import io.easybreezy.infrastructure.ktor.auth.Auth
import io.easybreezy.infrastructure.ktor.auth.UserPrincipal
import io.easybreezy.infrastructure.ktor.get
import io.easybreezy.infrastructure.ktor.post
import io.easybreezy.project.api.controller.ProjectController
import io.easybreezy.project.application.project.command.New
import io.easybreezy.project.application.project.command.NewRole
import io.easybreezy.project.application.project.command.NewTeam
import io.easybreezy.project.application.project.queryobject.Project
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
            authenticate(*Auth.user) {
                route("/api") {
                    projectRoutes(this)
                }
            }
        }
    }

    private fun projectRoutes(route: Route) {
        route.route("/projects") {
//            data class QueryParams(val id: UUID, val roleId: UUID)
            data class Slug(val slug: String)
            get<Response.Data<Project>, Slug>("/{slug}") { params ->
                controller<ProjectController>(this).show(params.slug)
            }

            post<Response.Either<Response.Ok, Response.Errors>, New>("") {
                    new -> controller<ProjectController>(this).create(new, resolvePrincipal<UserPrincipal>())
            }

            post<Response.Either<Response.Ok, Response.Errors>, NewRole, Slug>("/{slug}/roles") { command, params ->
                controller<ProjectController>(this).addRole(
                    params.slug,
                    command
                )
            }

            post<Response.Either<Response.Ok, Response.Errors>, NewTeam, Slug>("/{slug}/teams") { command, params ->
                controller<ProjectController>(this).addTeam(
                    params.slug,
                    command
                )
            }
//
//
//            get<Response.Ok, QueryParams>("/{id}/remove/role/{role_id}") { params ->
//                controller<ProjectController>(this).removeRole(params.id, params.roleId)
//            }
        }
    }
}
