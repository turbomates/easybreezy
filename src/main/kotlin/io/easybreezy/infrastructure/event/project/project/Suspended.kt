package io.easybreezy.infrastructure.event.project.project

import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.*

@Serializable
data class Suspended(
    @Serializable(with = UUIDSerializer::class) val project: UUID,
    @Serializable(with = LocalDateSerializer::class) val updatedAt: LocalDateTime
) : Event {
    override val key
        get() = Companion

    companion object : Event.Key<Suspended>
}
