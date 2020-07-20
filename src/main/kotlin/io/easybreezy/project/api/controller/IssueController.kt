package io.easybreezy.project.api.controller

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.infrastructure.query.pagingParameters
import io.easybreezy.infrastructure.structure.Either
import io.easybreezy.project.application.issue.command.ChangeStatus
import io.easybreezy.project.application.issue.command.AddComment
import io.easybreezy.project.application.issue.command.CreateSubIssue
import io.easybreezy.project.application.issue.command.Handler
import io.easybreezy.project.application.issue.command.New
import io.easybreezy.project.application.issue.command.Validation
import java.util.UUID
import io.easybreezy.infrastructure.upload.LocalFileStorage
import io.easybreezy.project.application.issue.command.AttachFiles
import io.easybreezy.project.application.issue.command.RemoveAttachmentFile
import io.easybreezy.project.application.issue.queryobject.Attachment
import io.easybreezy.project.application.issue.queryobject.Comment
import io.easybreezy.project.application.issue.queryobject.Issue
import io.easybreezy.project.application.issue.queryobject.IssueAttachmentsQO
import io.easybreezy.project.application.issue.queryobject.IssueCommentsQO
import io.easybreezy.project.application.issue.queryobject.IssueDetails
import io.easybreezy.project.application.issue.queryobject.IssueParticipants
import io.easybreezy.project.application.issue.queryobject.IssueParticipantsQO
import io.easybreezy.project.application.issue.queryobject.IssueQO
import io.easybreezy.project.application.issue.queryobject.IssueTiming
import io.easybreezy.project.application.issue.queryobject.IssueTimingQO
import io.easybreezy.project.application.issue.queryobject.IssuesQO
import io.easybreezy.project.infrastructure.AttachmentRepository

class IssueController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val queryExecutor: QueryExecutor,
    private val fileStorage: LocalFileStorage,
    private val transaction: TransactionManager,
    private val attachmentRepository: AttachmentRepository
) : Controller() {

    suspend fun create(command: New): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.validateCommand(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.newIssue(command)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun addComment(command: AddComment): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.validateCommand(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.addComment(command)
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

    suspend fun comments(id: UUID): Response.Data<Set<Comment>> {
        return Response.Data(
            queryExecutor.execute(IssueCommentsQO(id))
        )
    }

    suspend fun timing(id: UUID): Response.Data<IssueTiming> {
        return Response.Data(
            queryExecutor.execute(IssueTimingQO(id))
        )
    }

    suspend fun participants(id: UUID): Response.Data<IssueParticipants> {
        return Response.Data(
            queryExecutor.execute(IssueParticipantsQO(id))
        )
    }

    suspend fun list(project: String): Response.Listing<Issue> {
        return Response.Listing(
            queryExecutor.execute(IssuesQO(call.request.pagingParameters(), project))
        )
    }

    suspend fun attachments(id: UUID): Response.Data<Set<Attachment>> {
        return Response.Data(
            queryExecutor.execute(IssueAttachmentsQO(id))
        )
    }

    suspend fun attachFiles(command: AttachFiles, issueId: UUID): Response.Either<Response.Ok, Response.Errors> {
        command.issueId = issueId
        val errors = validation.validateCommand(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.attachFiles(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun removeAttachmentFile(issueId: UUID, fileId: UUID): Response.Either<Response.Ok, Response.Errors> {
        val command = RemoveAttachmentFile(issueId, fileId)
        handler.removeAttachmentFile(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun attachmentFile(issueId: UUID, fileId: UUID): Response.File = transaction {
        return@transaction Response.File(fileStorage.get(attachmentRepository[issueId].get(fileId)))
    }
}
