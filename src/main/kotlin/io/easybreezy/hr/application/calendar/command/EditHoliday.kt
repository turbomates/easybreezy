package io.easybreezy.hr.application.calendar.command

import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.LocalDate
import java.util.UUID

@Serializable
data class EditHoliday(
    @Serializable(with = LocalDateSerializer::class)
    val day: LocalDate,
    val name: String,
    val isWorkingDay: Boolean
) {
    @Transient
    lateinit var calendarId: UUID
}
