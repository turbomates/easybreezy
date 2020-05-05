package io.easybreezy.hr.api.controller

import com.google.inject.Inject
import io.easybreezy.hr.application.location.AssignLocation
import io.easybreezy.hr.application.location.CreateLocation
import io.easybreezy.hr.application.location.EditUserLocation
import io.easybreezy.hr.application.location.Handler
import io.easybreezy.hr.application.location.Validation
import io.easybreezy.hr.application.location.queryobject.Locations
import io.easybreezy.hr.application.location.queryobject.LocationsQO
import io.easybreezy.hr.application.location.queryobject.UserLocation
import io.easybreezy.hr.application.location.queryobject.UserLocationQO
import io.easybreezy.hr.application.location.queryobject.UserLocations
import io.easybreezy.hr.application.location.queryobject.UserLocationsQO
import io.easybreezy.hr.application.location.queryobject.UsersLocations
import io.easybreezy.hr.application.location.queryobject.UsersLocationsQO
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.structure.Either
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.query.QueryBus
import io.easybreezy.infrastructure.query.extractDateRange
import java.util.UUID

class LocationController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val queryBus: QueryBus
) : Controller() {

    suspend fun createLocation(command: CreateLocation): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onCreateLocation(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.handleCreateLocation(command)
        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun removeLocation(id: UUID): Response.Ok {
        handler.handleRemoveLocation(id)

        return Response.Ok
    }

    suspend fun locations(): Response.Data<Locations> {
        return Response.Data(queryBus(LocationsQO()))
    }

    suspend fun assignLocation(command: AssignLocation): Response.Either<Response.Ok, Response.Errors> {
        val errors = validation.onAssignLocation(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.handleAssignLocation(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun editUserLocation(id: UUID, command: EditUserLocation): Response.Either<Response.Ok, Response.Errors> {
        command.userLocationId = id
        val errors = validation.onEditUserLocation(command)
        if (errors.isNotEmpty()) {
            return Response.Either(Either.Right(Response.Errors(errors)))
        }
        handler.handleEditUserLocation(command)

        return Response.Either(Either.Left(Response.Ok))
    }

    suspend fun showUserLocation(id: UUID): Response.Data<UserLocation> {
        return Response.Data(queryBus(UserLocationQO(id)))
    }

    suspend fun userLocations(userId: UUID): Response.Data<UserLocations> {
        return Response.Data(
            queryBus(UserLocationsQO(userId))
        )
    }

    suspend fun usersLocations(): Response.Data<UsersLocations> {
        return Response.Data(
            queryBus(UsersLocationsQO(call.request.extractDateRange()))
        )
    }
}
