package io.easybreezy.hr.api.controller

import com.google.inject.Inject
import io.easybreezy.hr.application.location.AssignLocation
import io.easybreezy.hr.application.location.CreateLocation
import io.easybreezy.hr.application.location.EditUserLocation
import io.easybreezy.hr.application.location.Handler
import io.easybreezy.hr.application.location.Validation
import io.easybreezy.hr.application.location.queryobject.Location
import io.easybreezy.hr.application.location.queryobject.LocationsQO
import io.easybreezy.hr.application.location.queryobject.UserLocation
import io.easybreezy.hr.application.location.queryobject.UserLocationQO
import io.easybreezy.hr.application.location.queryobject.UserLocationsQO
import io.easybreezy.hr.infrastructure.LocationRepository
import io.easybreezy.hr.infrastructure.UserLocationRepository
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.structure.Either
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.infrastructure.query.pagingParameters
import java.util.UUID

class LocationController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val locationRepository: LocationRepository,
    private val userLocationRepository: UserLocationRepository,
    private val queryExecutor: QueryExecutor
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
        locationRepository.remove(id)
        return Response.Ok
    }

    suspend fun locations(): Response.Listing<Location> {
        return Response.Listing(
            queryExecutor.execute(LocationsQO(call.request.pagingParameters()))
        )
    }


    suspend fun assignLocation(userId: UUID, command: AssignLocation): Response.Either<Response.Ok, Response.Errors> {
        command.userId = userId
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

    suspend fun removeUserLocation(id: UUID): Response.Ok {
        userLocationRepository.remove(id)
        return Response.Ok
    }

    suspend fun showUserLocation(id: UUID): Response.Data<UserLocation> {
        return Response.Data(queryExecutor.execute(UserLocationQO(id)))
    }

    suspend fun userLocations(userId: UUID): Response.Listing<UserLocation> {
        return Response.Listing(
            queryExecutor.execute(UserLocationsQO(userId, call.request.pagingParameters()))
        )
    }
}