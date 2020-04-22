package io.easybreezy.hr.infrastructure

import io.easybreezy.hr.model.location.*
import org.jetbrains.exposed.sql.and
import java.util.UUID

class UserLocationRepository : UserLocation.Repository() {
    fun getOne(id: UUID): UserLocation {
        return find(id) ?: throw UserLocationNotFoundException(id)
    }

    fun findOneOpened(userId: UUID): UserLocation? {
        return find { UserLocations.userId eq userId and UserLocations.endedAt.isNull() }.firstOrNull()
    }

    private fun find(id: UUID): UserLocation? {
        return find { UserLocations.id eq id }.firstOrNull()
    }
}
