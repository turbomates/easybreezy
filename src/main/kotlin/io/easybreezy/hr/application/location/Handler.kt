package io.easybreezy.hr.application.location

import com.google.inject.Inject
import io.easybreezy.hr.infrastructure.LocationRepository
import io.easybreezy.hr.infrastructure.UserLocationRepository
import io.easybreezy.hr.model.location.Location
import io.easybreezy.hr.model.location.UserLocation
import io.easybreezy.infrastructure.exposed.TransactionManager
import java.util.UUID

class Handler @Inject constructor(
    private val locationRepository: LocationRepository,
    private val userLocationRepository: UserLocationRepository,
    private val transaction: TransactionManager
) {

    suspend fun handleCreateLocation(command: CreateLocation) = transaction {
        Location.create(command.name, command.vacationDays)
    }

    suspend fun handleAssignLocation(command: AssignLocation) = transaction {
        val openedLocation = userLocationRepository.findOneOpened(command.userId)
        openedLocation?.close()

        val location = locationRepository.getOne(command.locationId)
        val userLocation = UserLocation.create(
            command.startedAt,
            location,
            command.userId
        )
        command.extraVacationDays?.let { userLocation.addVacationDays(command.extraVacationDays) }
    }

    suspend fun handleEditUserLocation(command: EditUserLocation) = transaction {
        val userLocation = userLocationRepository.getOne(command.userLocationId)
        val location = locationRepository.getOne(command.locationId)

        userLocation.edit(command.startedAt, location)
    }

    suspend fun closeUserLocation(locationId: UUID) = transaction {
        val userLocation = userLocationRepository.getOne(locationId)
        userLocation.close()
    }

    suspend fun handleRemoveLocation(id: UUID) {
        transaction {
            locationRepository.remove(id)
        }
    }
}
