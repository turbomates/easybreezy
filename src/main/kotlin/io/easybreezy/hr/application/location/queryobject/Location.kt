package io.easybreezy.hr.application.location.queryobject

import io.easybreezy.hr.model.location.Locations as LocationsTable
import io.easybreezy.infrastructure.exposed.toUUID
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class LocationsQO : QueryObject<Locations> {
    override suspend fun getData() =
        transaction {
            val result = LocationsTable
                .selectAll()
                .map { it.toLocation() }

            Locations(result)
        }
}

internal fun ResultRow.toLocation() = Location(
    id = this[LocationsTable.id].toUUID(),
    name = this[LocationsTable.name]
)

@Serializable
data class Location(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String
)

@Serializable
data class Locations(
    val locations: List<Location>
)