package io.easybreezy.hr.infrastructure

import io.easybreezy.hr.model.location.UserLocation
import io.easybreezy.hr.model.location.UserLocationNotFoundException
import io.easybreezy.hr.model.location.UserLocations
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class UserLocationRepository : UserLocation.Repository() {
    fun getOne(id: UUID): UserLocation {
        return find(id) ?: throw UserLocationNotFoundException(id)
    }

    private fun find(id: UUID): UserLocation? {
        return transaction {
            find { UserLocations.id eq id }.firstOrNull()
        }
    }

    fun remove(id: UUID) {
        transaction {
            val location = find(id)
            location?.delete()
        }
    }
}
