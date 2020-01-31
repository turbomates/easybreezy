package io.easybreezy.infrastructure.event.project.project

import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.serialization.LocalDateTimeSerializer
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class Activated(
    @Serializable(with = UUIDSerializer::class) val project: UUID,
    @Serializable(with = LocalDateTimeSerializer::class) val updatedAt: LocalDateTime
) : Event {
    override val key
        get() = Companion

    companion object : Event.Key<Activated>
}
