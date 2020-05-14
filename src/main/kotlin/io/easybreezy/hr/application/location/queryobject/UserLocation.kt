package io.easybreezy.hr.application.location.queryobject

import io.easybreezy.hr.model.location.Locations
import io.easybreezy.hr.model.location.UserLocations as UserLocationsTable
import io.easybreezy.infrastructure.query.DateRange
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import io.easybreezy.user.model.*
import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import java.util.UUID

class UserLocationQO(private val userLocationId: UUID) : QueryObject<UserLocation> {
    override suspend fun getData() =
        (UserLocationsTable innerJoin Locations innerJoin Users).select {
            UserLocationsTable.id eq userLocationId
        }.first().toUserLocation()
}

class UserLocationsQO(private val dateRange: DateRange) : QueryObject<UserLocations> {
    override suspend fun getData() =
        UserLocationsTable
            .innerJoin(Locations)
            .innerJoin(Users)
            .selectAll()
            .andWhere { UserLocationsTable.startedAt greater dateRange.from }
            .andWhere { coalesce(UserLocationsTable.endedAt, UserLocationsTable.startedAt) less dateRange.to }
            .toUserLocations()
}

class IsUserLocationOwner(val id: UUID, val userId: UUID) : QueryObject<Boolean> {
    override suspend fun getData(): Boolean {
        return UserLocationsTable.select {
            UserLocationsTable.id eq id and (UserLocationsTable.userId eq userId)
        }.count() > 0
    }
}

private fun ResultRow.toUserLocation() = UserLocation(
    id = this[UserLocationsTable.id].value,
    startedAt = this[UserLocationsTable.startedAt].toString(),
    endedAt = this[UserLocationsTable.endedAt].toString(),
    location = this.toLocation(),
    userId = this[UserLocationsTable.userId].value,
    email = this[Users.email[EmailTable.email]],
    firstName = this[Users.name[NameTable.firstName]],
    lastName = this[Users.name[NameTable.lastName]]
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
    val userId: UUID,
    @ContextualSerialization
    val email: String?,
    val firstName: String?,
    val lastName: String?
)

@Serializable
data class UserLocations(
    val userLocations: Map<@Serializable(with = UUIDSerializer::class) UUID, List<UserLocation>>
)
