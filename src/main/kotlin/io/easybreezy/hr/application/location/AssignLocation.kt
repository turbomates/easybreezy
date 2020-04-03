package io.easybreezy.hr.application.location

import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
data class AssignLocation(
    @Serializable(with = LocalDateSerializer::class)
    val startedAt: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val endedAt: LocalDate,
    @Serializable(with = UUIDSerializer::class)
    val locationId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    val extraVacationDays: Int? = null
)
