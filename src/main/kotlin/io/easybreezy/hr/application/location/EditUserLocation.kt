package io.easybreezy.hr.application.location

import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.LocalDate
import java.util.UUID

@Serializable
data class EditUserLocation(
    @Serializable(with = LocalDateSerializer::class)
    val startedAt: LocalDate,
    @Serializable(with = UUIDSerializer::class)
    val locationId: UUID
) {
    @Transient
    lateinit var userLocationId: UUID
}
