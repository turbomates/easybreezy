package io.easybreezy.hr.application.location.queryobject

import io.easybreezy.hr.model.location.Locations
import io.easybreezy.infrastructure.exposed.toUUID
import io.easybreezy.infrastructure.query.ContinuousList
import io.easybreezy.infrastructure.query.PagingParameters
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.query.toContinuousList
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class LocationsQO(private val paging: PagingParameters) : QueryObject<ContinuousList<Location>> {
    override fun getData() =
        transaction {
            Locations
                .selectAll()
                .limit(paging.pageSize, paging.offset)
                .map { it.toLocation() }
                .toContinuousList(paging.pageSize, paging.currentPage)
        }
}

internal fun ResultRow.toLocation() = Location(
    id = this[Locations.id].toUUID(),
    name = this[Locations.name]
)

@Serializable
data class Location(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String
)