@file:UseSerializers(UUIDSerializer::class, LocalDateTimeSerializer::class)
package io.easybreezy.hr.application.event.command

import io.easybreezy.hr.model.event.EventId
import io.easybreezy.infrastructure.serialization.LocalDateTimeSerializer
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class UpdateDetails(
    val name: String? = null,
    val startedAt: LocalDateTime? = null,
    val description: String? = null,
    val endedAt: LocalDateTime? = null,
    val location: UUID? = null
) {
    @Transient
    lateinit var id: EventId
}
