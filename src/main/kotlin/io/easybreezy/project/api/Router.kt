package io.easybreezy.project.api

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.GenericPipeline
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.ktor.Router
import io.easybreezy.infrastructure.ktor.auth.Activity
import io.easybreezy.infrastructure.ktor.auth.Auth
import io.easybreezy.infrastructure.ktor.auth.UserPrincipal
import io.easybreezy.infrastructure.ktor.auth.authorize
import io.easybreezy.infrastructure.ktor.auth.containsAny
import io.easybreezy.infrastructure.ktor.get
import io.easybreezy.infrastructure.ktor.post
import io.easybreezy.infrastructure.ktor.postParams
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.project.api.controller.IssueController
import io.easybreezy.project.api.controller.ProjectController
import io.easybreezy.project.api.controller.TeamController
import io.easybreezy.project.application.issue.queryobject.Issue
import io.easybreezy.project.application.member.queryobject.IsTeamMember
import io.easybreezy.project.application.member.queryobject.MemberActivities
import io.easybreezy.project.application.project.command.ChangeRole
import io.easybreezy.project.application.project.command.RemoveRole
import io.easybreezy.project.application.project.command.NewRole
import io.easybreezy.project.application.project.command.New
import io.easybreezy.project.application.project.command.WriteDescription
import io.easybreezy.project.application.project.command.ChangeSlug
import io.easybreezy.project.application.project.command.RemoveStatus
import io.easybreezy.project.application.project.command.ChangeStatus
import io.easybreezy.project.application.project.command.NewStatus
import io.easybreezy.project.application.project.command.RemoveCategory
import io.easybreezy.project.application.project.command.ChangeCategory
import io.easybreezy.project.application.project.command.NewCategory
import io.easybreezy.project.application.project.queryobject.Project
import io.easybreezy.project.application.team.command.ActivateTeam
import io.easybreezy.project.application.team.command.ChangeMemberRole
import io.easybreezy.project.application.team.command.CloseTeam
import io.easybreezy.project.application.team.command.NewMember
import io.easybreezy.project.application.team.command.NewTeam
import io.easybreezy.project.application.team.command.RemoveMember
import io.easybreezy.project.application.team.queryobject.Team
import io.easybreezy.project.model.team.Role
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.locations.locations
import io.ktor.routing.Route
import io.ktor.routing.route
import io.ktor.routing.routing
import kotlinx.serialization.Serializable
import java.util.UUID
import io.easybreezy.project.application.issue.command.New as NewIssue

class Router @Inject constructor(
    application: Application,
    genericPipeline: GenericPipeline,
    private val queryExecutor: QueryExecutor
) : Router(application, genericPipeline) {

    init {
        application.routing {
            authenticate(*Auth.user) {
                route("/api/projects") {
                    projectRoutes()
                }
                route("/api/teams") {
                    teamRoutes()
                }
            }
        }
    }

    private fun Route.teamRoutes() {
        data class TeamId(val teamId: UUID)
        authorize(setOf(Activity.PROJECTS_MANAGE)) {
            data class TeamMember(val teamId: UUID, val memberId: UUID)
            post<Response.Either<Response.Ok, Response.Errors>, NewTeam>("/add") { command ->
                controller<TeamController>(this).newTeam(command)
            }
            post<Response.Either<Response.Ok, Response.Errors>, NewMember, TeamId>("/{teamId}/members/add") { command, params ->
                command.team = params.teamId
                controller<TeamController>(this).newMember(command)
            }
            post<Response.Ok, ActivateTeam, TeamId>("/{teamId}/activate") { _, params ->
                controller<TeamController>(this).activate(params.teamId)
            }
            post<Response.Ok, CloseTeam, TeamId>("/{teamId}/close") { _, params ->
                controller<TeamController>(this).close(params.teamId)
            }
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

        authorize(setOf(Activity.PROJECTS_SHOW), {
            val principal = principal<UserPrincipal>()
            principal?.let {
                val teamId = locations.resolve<TeamId>(this).teamId
                queryExecutor.execute(IsTeamMember(principal.id, teamId))
            } ?: false
        }) {
            get<Response.Data<Team>, TeamId>("/{teamId}") { params ->
                controller<TeamController>(this).show(params.teamId)
            }
        }
    }

    private fun Route.projectRoutes() {
        @Serializable
        data class NewRequest(val name: String, val description: String, val slug: String? = null) {
            fun makeCommand(principal: UUID): New {
                return New(principal, name, description, slug)
            }
        }

        authorize(setOf(Activity.PROJECTS_MANAGE)) {
            post<Response.Either<Response.Ok, Response.Errors>, NewRequest>("") { request ->
                controller<ProjectController>(this).create(request.makeCommand(resolvePrincipal<UserPrincipal>()))
            }
        }
        authorize(setOf(Activity.PROJECTS_SHOW)) {
            get<Response.Listing<Project>>("") {
                controller<ProjectController>(this).list()
            }
        }

        get<Response.Listing<Project>>("/my") {
            controller<ProjectController>(this).myList(resolvePrincipal<UserPrincipal>())
        }

        get<Response.Data<List<Role.Permission>>>("/permissions") {
            controller<ProjectController>(this).permissions()
        }

        route("/{slug}") {

            authorize(setOf(Activity.PROJECTS_SHOW), { memberHasAccess(setOf(Activity.PROJECTS_SHOW)) }) {
                post<Response.Either<Response.Ok, Response.Errors>, NewIssue, SlugParam>("/issues/add") { new, params ->
                    new.project = params.slug
                    new.author = resolvePrincipal<UserPrincipal>()
                    controller<IssueController>(this).create(new)
                }
                get<Response.Listing<Issue>, SlugParam>("/issues") { params ->
                    controller<IssueController>(this).list(params.slug)
                }

                get<Response.Data<Project>, SlugParam>("") { params ->
                    controller<ProjectController>(this).show(params.slug)
                }
            }

            authorize(setOf(Activity.PROJECTS_MANAGE)) {
                post<Response.Either<Response.Ok, Response.Errors>, SlugParam, SlugParam>("/change-slug") { new, params ->
                    controller<ProjectController>(this).changeSlug(ChangeSlug(params.slug, new.slug))
                }
                postParams<Response.Ok, SlugParam>("/activate") { params ->
                    controller<ProjectController>(this).activate(params.slug)
                }
                postParams<Response.Ok, SlugParam>("/suspend") { params ->
                    controller<ProjectController>(this).suspendProject(params.slug)
                }
                postParams<Response.Ok, SlugParam>("/close") { params ->
                    controller<ProjectController>(this).close(params.slug)
                }
                post<Response.Either<Response.Ok, Response.Errors>, WriteDescription, SlugParam>("/write-description") { command, params ->
                    command.project = params.slug
                    controller<ProjectController>(this).writeDescription(command)
                }
                data class ProjectRole(val slug: String, val roleId: UUID)
                post<Response.Either<Response.Ok, Response.Errors>, NewRole, SlugParam>("/roles/add") { command, params ->
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
                data class ProjectCategory(val slug: String, val categoryId: UUID)
                post<Response.Either<Response.Ok, Response.Errors>, NewCategory, SlugParam>("/categories/add") { command, params ->
                    command.project = params.slug
                    controller<ProjectController>(this).addCategory(command)
                }
                post<Response.Either<Response.Ok, Response.Errors>, ChangeCategory, ProjectCategory>(
                    "/categories/{categoryId}/change"
                ) { command, params ->
                    command.project = params.slug
                    command.categoryId = params.categoryId
                    controller<ProjectController>(this).changeCategory(command)
                }
                post<Response.Either<Response.Ok, Response.Errors>, RemoveCategory, ProjectCategory>(
                    "/categories/{categoryId}/remove"
                ) { command, params ->
                    command.categoryId = params.categoryId
                    command.project = params.slug
                    controller<ProjectController>(this).removeCategory(command)
                }
                data class ProjectStatus(val slug: String, val statusId: UUID)
                post<Response.Either<Response.Ok, Response.Errors>, NewStatus, SlugParam>("/statuses/add") { command, params ->
                    command.project = params.slug
                    controller<ProjectController>(this).addStatus(command)
                }
                post<Response.Either<Response.Ok, Response.Errors>, ChangeStatus, ProjectStatus>("/statuses/{statusId}/change") { command, params ->
                    command.project = params.slug
                    command.statusId = params.statusId
                    controller<ProjectController>(this).changeStatus(command)
                }
                post<Response.Either<Response.Ok, Response.Errors>, RemoveStatus, ProjectStatus>("/statuses/{statusId}/remove") { command, params ->
                    command.statusId = params.statusId
                    command.project = params.slug
                    controller<ProjectController>(this).removeStatus(command)
                }
            }
        }
    }

    @Serializable
    data class SlugParam(val slug: String)

    private suspend fun ApplicationCall.memberHasAccess(activities: Set<Activity>): Boolean {
        val principal = principal<UserPrincipal>()
        if (principal === null) {
            return false
        }
        val memberActivities =
            queryExecutor.execute(MemberActivities(principal.id, locations.resolve<SlugParam>(this).slug))
        return memberActivities.containsAny(activities)
    }
}
