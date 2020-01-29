package io.easybreezy.hr.application.location

import com.google.inject.Inject
import io.easybreezy.hr.infrastructure.LocationRepository
import io.easybreezy.hr.infrastructure.UserLocationRepository
import io.easybreezy.hr.model.location.Location
import io.easybreezy.hr.model.location.UserLocation

class Handler @Inject constructor(
    private val locationRepository: LocationRepository,
    private val userLocationRepository: UserLocationRepository
) {

    fun handleCreateLocation(command: CreateLocation) {
        Location.create(command.name)
    }

    fun handleAssignLocation(command: AssignLocation) {
        val location = locationRepository.getOne(command.locationId)
        UserLocation.create(
            command.startedAt,
            command.endedAt,
            location,
            command.userId
        )
    }

    fun handleEditUserLocation(command: EditUserLocation) {
        val userLocation = userLocationRepository.getOne(command.userLocationId)
        val location = locationRepository.getOne(command.locationId)

        userLocation.edit(command.startedAt, command.endedAt, location)
    }
}