package io.easybreezy.hr.infrastructure

import io.easybreezy.hr.model.location.Location
import io.easybreezy.hr.model.location.LocationNotFoundException
import io.easybreezy.hr.model.location.Locations
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class LocationRepository : Location.Repository() {
    fun getOne(id: UUID): Location {
        return find(id) ?: throw LocationNotFoundException(id)
    }

    private fun find(id: UUID): Location? {
        return transaction {
            find { Locations.id eq id }.firstOrNull()
        }
    }

    fun remove(id: UUID) {
        transaction {
            val location = find(id)
            location?.delete()
        }
    }
}