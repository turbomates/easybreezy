package io.easybreezy.hr.application.calendar.queryobject

import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import java.time.LocalDate
import io.easybreezy.hr.model.calendar.Holidays as HolidaysTable

class HolidaysQO() : QueryObject<Holidays> {
    override suspend fun getData(): Holidays {
        val result = HolidaysTable
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
