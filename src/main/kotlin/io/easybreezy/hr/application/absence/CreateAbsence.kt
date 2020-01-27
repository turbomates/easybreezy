package io.easybreezy.hr.application.absence

import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
data class CreateAbsence(
    @Serializable(with = LocalDateSerializer::class)
    val startedAt: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val endedAt: LocalDate,
    val reason: String,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    val comment: String? = null,
    val location: String? = null
)
