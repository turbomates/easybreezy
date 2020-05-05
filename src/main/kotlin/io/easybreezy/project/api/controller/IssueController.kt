package io.easybreezy.project.api.controller

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.query.QueryBus
import io.easybreezy.infrastructure.query.pagingParameters
import io.easybreezy.infrastructure.structure.Either
import io.easybreezy.project.application.issue.command.Handler
import io.easybreezy.project.application.issue.command.New
import io.easybreezy.project.application.issue.command.Update
import io.easybreezy.project.application.issue.command.Validation
import io.easybreezy.project.application.issue.queryobject.Issue
import io.easybreezy.project.application.issue.queryobject.IssueQO
import io.easybreezy.project.application.issue.queryobject.IssuesQO
import java.util.UUID

class IssueController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val queryBus: QueryBus
) : Controller() {

    suspend fun create(command: New): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.validateCommand(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.newIssue(command)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun update(command: Update): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.validateCommand(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.update(command)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun show(id: UUID): Response.Data<Issue> {
        return Response.Data(
            queryBus(IssueQO(id))
        )
    }

    suspend fun list(project: String): Response.Listing<Issue> {
        return Response.Listing(
            queryBus(IssuesQO(call.request.pagingParameters()))
        )
    }
}
