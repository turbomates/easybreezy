package io.easybreezy.hr.application.calendar.command

import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID
import kotlinx.serialization.Transient

@Serializable
class EditCalendar(
    val name: String,
    @Serializable(with = UUIDSerializer::class)
    val locationId: UUID
) {
    @Transient
    lateinit var id: UUID
}
