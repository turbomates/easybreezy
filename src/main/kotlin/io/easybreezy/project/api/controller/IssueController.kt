package io.easybreezy.project.api.controller

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.infrastructure.query.pagingParameters
import io.easybreezy.infrastructure.structure.Either
import io.easybreezy.infrastructure.upload.Path
import io.easybreezy.project.application.issue.FileStorage
import io.easybreezy.project.application.issue.command.AddFiles
import io.easybreezy.project.application.issue.command.Handler
import io.easybreezy.project.application.issue.command.New
import io.easybreezy.project.application.issue.command.RemoveFile
import io.easybreezy.project.application.issue.command.Update
import io.easybreezy.project.application.issue.command.Validation
import io.easybreezy.project.application.issue.queryobject.Issue
import io.easybreezy.project.application.issue.queryobject.IssueDetails
import io.easybreezy.project.application.issue.queryobject.IssueQO
import io.easybreezy.project.application.issue.queryobject.IssuesQO
import java.util.UUID

class IssueController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val queryExecutor: QueryExecutor,
    private val fileStorage: FileStorage
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

    suspend fun addFiles(command: AddFiles, issueId: UUID): Response.Either<Response.Ok, Response.Errors> {
        command.issueId = issueId
        val errors = validation.validateCommand(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.addFiles(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun removeFile(issueId: UUID, path: Path): Response.Either<Response.Ok, Response.Errors> {
        val command = RemoveFile(issueId, path)
        val errors = validation.validateCommand(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.removeFile(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun issueFile(issueId: UUID, path: Path): Response.File {
        return Response.File(fileStorage.get(issueId, path))
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
