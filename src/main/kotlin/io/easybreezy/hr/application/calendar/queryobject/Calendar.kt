package io.easybreezy.hr.application.calendar.queryobject

import io.easybreezy.hr.application.location.queryobject.Location
import io.easybreezy.hr.application.location.queryobject.toLocation
import io.easybreezy.hr.model.location.Locations
import io.easybreezy.hr.model.calendar.Calendars as CalendarsTable
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

class CalendarsQO() : QueryObject<Calendars> {
    override suspend fun getData(): Calendars {
        val result = (CalendarsTable innerJoin Locations)
            .selectAll()
            .map { it.toCalendar() }

        return Calendars(result)
    }
}

private fun ResultRow.toCalendar() = Calendar(
    this[CalendarsTable.id].value,
    this[CalendarsTable.name],
    this.toLocation()
)

@Serializable
data class Calendar(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val location: Location
)

@Serializable
data class Calendars(val calendars: List<Calendar>)
