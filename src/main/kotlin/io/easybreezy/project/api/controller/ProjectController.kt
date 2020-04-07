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

        val errors = validation.validateCommand(new)
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

    suspend fun activate(slug: String): Response.Ok {
        handler.activate(slug)
        return Response.Ok
    }

    suspend fun close(slug: String): Response.Ok {
        handler.close(slug)
        return Response.Ok
    }

    suspend fun suspendProject(slug: String): Response.Ok {
        handler.suspendProject(slug)
        return Response.Ok
    }

    suspend fun writeDescription(command: WriteDescription): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.validateCommand(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.writeDescription(command)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun addRole(command: NewRole): Response.Either<Response.Ok, Response.Errors> {

        val errors = validation.validateCommand(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.addRole(command)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun changeRole(command: ChangeRole): Response.Either<Response.Ok, Response.Errors> {

        val errors = validation.validateCommand(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.changeRole(command)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun removeRole(command: RemoveRole): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.validateCommand(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }

        handler.removeRole(command)
        return Response.Either(Either.Left(Response.Ok))
    }
}
