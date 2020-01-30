package io.easybreezy.hr.api.controller

import com.google.inject.Inject
import io.easybreezy.hr.application.location.AssignLocation
import io.easybreezy.hr.application.location.CreateLocation
import io.easybreezy.hr.application.location.EditUserLocation
import io.easybreezy.hr.application.location.Handler
import io.easybreezy.hr.application.location.Validation
import io.easybreezy.hr.application.location.queryobject.LocationsQO
import io.easybreezy.hr.application.location.queryobject.UserLocationQO
import io.easybreezy.hr.application.location.queryobject.UserLocationsQO
import io.easybreezy.hr.infrastructure.LocationRepository
import io.easybreezy.hr.infrastructure.UserLocationRepository
import io.easybreezy.infrastructure.ktor.Controller
import io.easybreezy.infrastructure.ktor.respondListing
import io.easybreezy.infrastructure.ktor.respondOk
import io.easybreezy.infrastructure.ktor.respondWith
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.infrastructure.query.pagingParameters
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class LocationController @Inject constructor(
    private val handler: Handler,
    private val validation: Validation,
    private val locationRepository: LocationRepository,
    private val userLocationRepository: UserLocationRepository,
    private val queryExecutor: QueryExecutor
) : Controller() {

    suspend fun createLocation(command: CreateLocation) {
        validation.onCreateLocation(command)
        transaction {
            handler.handleCreateLocation(command)
        }

        call.respondOk()
    }

    suspend fun removeLocation(id: UUID) {
        locationRepository.remove(id)

        call.respondOk()
    }

    suspend fun locations() {
        call.respondListing(
            queryExecutor.execute(LocationsQO(call.request.pagingParameters()))
        )
    }


    suspend fun assignLocation(userId: UUID, command: AssignLocation) {
        command.userId = userId
        validation.onAssignLocation(command)
        transaction {
            handler.handleAssignLocation(command)
        }

        call.respondOk()
    }

    suspend fun editUserLocation(id: UUID, command: EditUserLocation) {
        command.userLocationId = id
        validation.onEditUserLocation(command)
        transaction {
            handler.handleEditUserLocation(command)
        }

        call.respondOk()
    }

    suspend fun removeUserLocation(id: UUID) {
        userLocationRepository.remove(id)

        call.respondOk()
    }

    suspend fun showUserLocation(id: UUID) {
        call.respondWith {
            data = queryExecutor.execute(UserLocationQO(id))
        }
    }

    suspend fun userLocations(userId: UUID) {
        call.respondListing(
            queryExecutor.execute(UserLocationsQO(userId, call.request.pagingParameters()))
        )
    }
}