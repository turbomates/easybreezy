@file:UseSerializers(LocalDateTimeSerializer::class)
package io.easybreezy.infrastructure.event.project.issue

import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.serialization.LocalDateTimeSerializer
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class DueDateChanged(
    @Serializable(with = UUIDSerializer::class) val issue: UUID,
    val previous: LocalDateTime?,
    val current: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class) val at: LocalDateTime
) : Event {
    override val key
        get() = Companion

    companion object : Event.Key<DueDateChanged>
}
