package io.easybreezy.project.api.controller

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.infrastructure.structure.Either
import io.easybreezy.project.application.team.command.*
import io.easybreezy.project.application.team.queryobject.Team
import io.easybreezy.project.application.team.queryobject.TeamQO
import java.util.UUID

class TeamController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val queryExecutor: QueryExecutor
) : Controller() {

    suspend fun newTeam(command: NewTeam): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.validate(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.newTeam(command)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun newMember(command: NewMember): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.validate(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.newMember(command)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun show(id: UUID): Response.Data<Team> {
        return Response.Data(
            queryExecutor.execute(TeamQO(id))
        )
    }

    suspend fun close(team: UUID): Response.Ok {
        handler.close(team)
        return Response.Ok
    }

    suspend fun activate(team: UUID): Response.Ok {
        handler.activate(team)
        return Response.Ok
    }

    suspend fun removeMember(command: RemoveMember): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.validate(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.removeMember(command)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun changeMemberRole(command: ChangeMemberRole): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.validate(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.changeMemberRole(command)
        return Response.Either(Either.Left(Response.Ok))
    }
}
