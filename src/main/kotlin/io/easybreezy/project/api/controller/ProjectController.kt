package io.easybreezy.project.api.controller

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.infrastructure.query.pagingParameters
import io.easybreezy.infrastructure.structure.Either
import io.easybreezy.project.application.project.command.*
import io.easybreezy.project.application.project.queryobject.Project
import io.easybreezy.project.application.project.queryobject.ProjectQO
import io.easybreezy.project.application.project.queryobject.ProjectsQO
import java.util.UUID

class ProjectController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val queryExecutor: QueryExecutor
) : Controller() {

    suspend fun create(new: New, author: UUID): Response.Either<Response.Ok, Response.Errors> {

        val errors = validation.validate(new)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.new(new, author)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun list(): Response.Listing<Project> {
        return Response.Listing(
            queryExecutor.execute(ProjectsQO(call.request.pagingParameters()))
        )
    }

    suspend fun show(slug: String): Response.Data<Project> {
        return Response.Data(
            queryExecutor.execute(ProjectQO(slug))
        )
    }

    suspend fun removeRole(id: UUID, roleId: UUID): Response.Ok {
        return Response.Ok
    }

    suspend fun addRole(slug: String, command: NewRole): Response.Either<Response.Ok, Response.Errors> {

        val errors = validation.validate(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.addRole(command, slug)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun addTeam(slug: String, command: NewTeam): Response.Either<Response.Ok, Response.Errors> {

        val errors = validation.validate(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.addTeam(command, slug)
        return Response.Either(Either.Left(Response.Ok))
    }
}
