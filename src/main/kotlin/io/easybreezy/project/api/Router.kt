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
import io.easybreezy.project.api.controller.TeamController
import io.easybreezy.project.application.project.command.*
import io.easybreezy.project.application.team.command.*
import io.ktor.application.Application
import io.ktor.auth.authenticate
import io.ktor.routing.Route
import io.ktor.routing.route
import io.ktor.routing.routing
import java.util.*

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

            post<Response.Either<Response.Ok, Response.Errors>, New>("") {
                    new -> controller<ProjectController>(this).create(new, resolvePrincipal<UserPrincipal>())
            }
        }

        route.route("/projects/{slug}") {
            data class Project(val slug: String)

            get<Response.Data<io.easybreezy.project.application.project.queryobject.Project>, Project>("") { params ->
                controller<ProjectController>(this).show(params.slug)
            }

            post<Response.Ok, Activate, Project>("/activate") { _, params ->
                controller<ProjectController>(this).activate(params.slug)
            }
            post<Response.Ok, Suspend, Project>("/suspend") { _, params ->
                controller<ProjectController>(this).suspendProject(params.slug)
            }
            post<Response.Ok, Close, Project>("/close") { _, params ->
                controller<ProjectController>(this).close(params.slug)
            }
            post<Response.Either<Response.Ok, Response.Errors>, WriteDescription, Project>("/write-description") { command, params ->
                command.project = params.slug
                controller<ProjectController>(this).writeDescription(command)
            }

            data class ProjectRole(val slug: String, val roleId: UUID)
            post<Response.Either<Response.Ok, Response.Errors>, NewRole, Project>("/roles/add") { command, params ->
                command.project = params.slug
                controller<ProjectController>(this).addRole(command)
            }
            post<Response.Either<Response.Ok, Response.Errors>, ChangeRole, ProjectRole>("/roles/{roleId}/change") { command, params ->
                command.project = params.slug
                command.roleId = params.roleId
                controller<ProjectController>(this).changeRole(command)
            }
            post<Response.Either<Response.Ok, Response.Errors>, RemoveRole, ProjectRole>("/roles/{roleId}/remove") { command, params ->
                command.roleId = params.roleId
                command.project = params.slug
                controller<ProjectController>(this).removeRole(command)
            }
        }

        route.route("/teams") {
            data class Team(val teamId: UUID)

            post<Response.Either<Response.Ok, Response.Errors>, NewTeam>("/add") { command ->
                controller<TeamController>(this).newTeam(command)
            }
            get<Response.Data<io.easybreezy.project.application.team.queryobject.Team>, Team>("/{teamId}") { params ->
                controller<TeamController>(this).show(params.teamId)
            }
            post<Response.Either<Response.Ok, Response.Errors>, NewMember, Team>("/{teamId}/members/add") { command, params ->
                command.team = params.teamId
                controller<TeamController>(this).newMember(command)
            }

            post<Response.Ok, ActivateTeam, Team>("/{teamId}/activate") { _, params ->
                controller<TeamController>(this).activate(params.teamId)
            }

            post<Response.Ok, CloseTeam, Team>("/{teamId}/close") { _, params ->
                controller<TeamController>(this).close(params.teamId)
            }

            data class TeamMember(val teamId: UUID, val memberId: UUID)
            post<Response.Either<Response.Ok, Response.Errors>, RemoveMember, TeamMember>("/{teamId}/members/{memberId}/remove") { command, params ->
                command.memberId = params.memberId
                command.team = params.teamId
                controller<TeamController>(this).removeMember(command)
            }

            post<Response.Either<Response.Ok, Response.Errors>, ChangeMemberRole, TeamMember>("/{teamId}/members/{memberId}/change-role") { command, params ->
                command.team = params.teamId
                command.memberId = params.memberId
                controller<TeamController>(this).changeMemberRole(command)
            }
        }
    }
}
