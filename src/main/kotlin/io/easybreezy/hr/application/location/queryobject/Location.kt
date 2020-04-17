package io.easybreezy.hr.application.location.queryobject

import io.easybreezy.hr.model.location.Locations as LocationsTable
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

class LocationsQO : QueryObject<Locations> {
    override suspend fun getData(): Locations {
        val result = LocationsTable
            .selectAll()
            .map { it.toLocation() }

        return Locations(result)
    }
}

internal fun ResultRow.toLocation() = Location(
    id = this[LocationsTable.id].value,
    name = this[LocationsTable.name],
    vacationDays = this[LocationsTable.vacationDays]
)

@Serializable
data class Location(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val vacationDays: Int
)

@Serializable
data class Locations(
    val locations: List<Location>
)
