@file:UseSerializers(UUIDSerializer::class)
package io.easybreezy.infrastructure.event.project.issue

import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.serialization.LocalDateTimeSerializer
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class Created(
    val project: UUID,
    val issue: UUID,
    val by: UUID,
    val title: String,
    val description: String?,
    @Serializable(with = LocalDateTimeSerializer::class) val at: LocalDateTime
) : Event {
    override val key
        get() = Companion

    companion object : Event.Key<Created>
}
