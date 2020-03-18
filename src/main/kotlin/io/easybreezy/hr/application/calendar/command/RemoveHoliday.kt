package io.easybreezy.hr.application.calendar.command

import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
data class RemoveHoliday(
    @Serializable(with = UUIDSerializer::class)
    val calendarId: UUID,
    @Serializable(with = LocalDateSerializer::class)
    val day: LocalDate
)
