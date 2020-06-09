package io.easybreezy.project.api.controller

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.infrastructure.query.pagingParameters
import io.easybreezy.infrastructure.structure.Either
import io.easybreezy.project.application.issue.command.ChangeStatus
import io.easybreezy.project.application.issue.command.CommentUpdate
import io.easybreezy.project.application.issue.command.CreateSubIssue
import io.easybreezy.project.application.issue.command.Handler
import io.easybreezy.project.application.issue.command.New
import io.easybreezy.project.application.issue.command.Validation
import io.easybreezy.project.application.issue.queryobject.Issue
import io.easybreezy.project.application.issue.queryobject.IssueDetails
import io.easybreezy.project.application.issue.queryobject.IssueQO
import io.easybreezy.project.application.issue.queryobject.IssuesQO
import java.util.UUID

class IssueController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val queryExecutor: QueryExecutor
) : Controller() {

    suspend fun create(command: New): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.validateCommand(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.newIssue(command)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun commentUpdate(command: CommentUpdate): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.validateCommand(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.update(command)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun changeStatus(command: ChangeStatus): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.validateCommand(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.changeStatus(command)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun subIssue(command: CreateSubIssue): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.validateCommand(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.createSubIssue(command)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun show(id: UUID): Response.Data<IssueDetails> {
        return Response.Data(
            queryExecutor.execute(IssueQO(id))
        )
    }

    suspend fun list(project: String): Response.Listing<Issue> {
        return Response.Listing(
            queryExecutor.execute(IssuesQO(call.request.pagingParameters(), project))
        )
    }
}
