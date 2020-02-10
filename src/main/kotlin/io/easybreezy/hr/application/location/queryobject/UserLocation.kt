package io.easybreezy.hr.application.location.queryobject

import io.easybreezy.hr.model.location.Locations
import io.easybreezy.hr.model.location.UserLocations
import io.easybreezy.infrastructure.exposed.toUUID
import io.easybreezy.infrastructure.query.ContinuousList
import io.easybreezy.infrastructure.query.PagingParameters
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.query.toContinuousList
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID


class UserLocationQO(private val userLocationId: UUID) : QueryObject<UserLocation> {
    override suspend fun getData() =
        transaction {
            (UserLocations innerJoin Locations).select {
                UserLocations.id eq userLocationId
            }.first().toUserLocation()
        }
}

class UserLocationsQO(private val userId: UUID, private val paging: PagingParameters) :
    QueryObject<ContinuousList<UserLocation>> {
    override suspend fun getData() =
        transaction {
            (UserLocations innerJoin Locations)
                .selectAll()
                .andWhere { UserLocations.userId eq userId }
                .limit(paging.pageSize, paging.offset)
                .map { it.toUserLocation() }
                .toContinuousList(paging.pageSize, paging.currentPage)
        }
}

private fun ResultRow.toUserLocation() = UserLocation(
    id = this[UserLocations.id].toUUID(),
    startedAt = this[UserLocations.startedAt].toString(),
    endedAt = this[UserLocations.endedAt].toString(),
    location = this.toLocation(),
    userId = this[UserLocations.userId]
)

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

