package io.easybreezy.hr.application.absence

import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.LocalDate
import java.util.UUID

@Serializable
data class UpdateAbsence(
    @Serializable(with = LocalDateSerializer::class)
    val startedAt: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val endedAt: LocalDate,
    val reason: String,
    val comment: String? = null,
    val location: String? = null
) {
    @Transient
    lateinit var id: UUID
}
