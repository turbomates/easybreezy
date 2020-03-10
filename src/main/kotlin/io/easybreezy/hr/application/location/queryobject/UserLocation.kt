package io.easybreezy.hr.application.location.queryobject

import io.easybreezy.hr.model.location.Locations
import io.easybreezy.hr.model.location.UserLocations as UserLocationsTable
import io.easybreezy.infrastructure.exposed.toUUID
import io.easybreezy.infrastructure.query.DateRange
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

class UserLocationQO(private val userLocationId: UUID) : QueryObject<UserLocation> {
    override suspend fun getData() =
        (UserLocationsTable innerJoin Locations).select {
            UserLocationsTable.id eq userLocationId
        }.first().toUserLocation()
}

class UserLocationsQO(private val dateRange: DateRange) : QueryObject<UserLocations> {
    override suspend fun getData() =
        (UserLocationsTable innerJoin Locations)
            .selectAll()
            .andWhere { UserLocationsTable.startedAt greater dateRange.from }
            .andWhere { UserLocationsTable.endedAt less dateRange.to }
            .toUserLocations()
}

private fun ResultRow.toUserLocation() = UserLocation(
    id = this[UserLocationsTable.id].toUUID(),
    startedAt = this[UserLocationsTable.startedAt].toString(),
    endedAt = this[UserLocationsTable.endedAt].toString(),
    location = this.toLocation(),
    userId = this[UserLocationsTable.userId]
)

private fun Iterable<ResultRow>.toUserLocations(): UserLocations {
    val data = fold(mutableMapOf<UUID, MutableList<UserLocation>>()) { map, resultRaw ->
        val userLocation = resultRaw.toUserLocation()
        if (map.containsKey(userLocation.userId)) map[userLocation.userId]?.add(userLocation)
        else map[userLocation.userId] = mutableListOf(userLocation)

        map
    }

    return UserLocations(data)
}

@Serializable
data class UserLocation(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val startedAt: String,
    val endedAt: String,
    val location: Location,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID
)

@Serializable
data class UserLocations(
    val userLocations: Map<@Serializable(with = UUIDSerializer::class) UUID, List<UserLocation>>
)

