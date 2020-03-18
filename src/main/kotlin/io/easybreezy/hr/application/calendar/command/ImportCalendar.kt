package io.easybreezy.hr.application.calendar.command

import io.easybreezy.hr.model.location.LocationId
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable

@Serializable
data class ImportCalendar(
    val encodedCalendar: String,
    val name: String,
    @Serializable(with = UUIDSerializer::class)
    val locationId: LocationId
)
