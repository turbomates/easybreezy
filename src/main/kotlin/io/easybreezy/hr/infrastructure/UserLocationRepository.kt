package io.easybreezy.hr.infrastructure

import io.easybreezy.hr.model.location.UserLocation
import io.easybreezy.hr.model.location.UserLocationNotFoundException
import io.easybreezy.hr.model.location.UserLocations
import java.util.UUID

class UserLocationRepository : UserLocation.Repository() {
    fun getOne(id: UUID): UserLocation {
        return find(id) ?: throw UserLocationNotFoundException(id)
    }

    private fun find(id: UUID): UserLocation? {
        return find { UserLocations.id eq id }.firstOrNull()
    }
}
