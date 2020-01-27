package io.easybreezy.hr.application.absence

import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class WorkingHour(
    @Serializable(with = LocalDateSerializer::class)
    val day: LocalDate,
    val count: Int
)
