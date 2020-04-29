package io.easybreezy.hr.application.calendar.queryobject

import io.easybreezy.hr.model.calendar.Calendars
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import java.time.LocalDate
import java.util.*
import io.easybreezy.hr.model.calendar.Holidays as HolidaysTable

class HolidaysQO(private val calendarId: UUID) : QueryObject<Holidays> {
    override suspend fun getData(): Holidays {
        val result = HolidaysTable
            .join(Calendars, JoinType.INNER, additionalConstraint = { Calendars.id eq calendarId })
            .selectAll()
            .map { it.toHoliday() }

        return Holidays(result)
    }
}

private fun ResultRow.toHoliday() = Holiday(
    this[HolidaysTable.name],
    this[HolidaysTable.day],
    this[HolidaysTable.isWorkingDay]
)

@Serializable
data class Holiday(
    val name: String,
    @Serializable(with = LocalDateSerializer::class) val day: LocalDate,
    val isWorkingDay: Boolean
)

@Serializable
data class Holidays(val holidays: List<Holiday>)
