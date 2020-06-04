@file:UseSerializers(UUIDSerializer::class, LocalDateTimeSerializer::class)
package io.easybreezy.infrastructure.event.event

import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.serialization.LocalDateTimeSerializer
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime
import java.util.UUID

@Serializable
class StartChanged(
    val event: UUID,
    val oldStartedAt: LocalDateTime,
    val currentStartedAt: LocalDateTime
) : Event {
    override val key
        get() = Companion

    companion object : Event.Key<StartChanged>
}
