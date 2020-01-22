package io.easybreezy.infrastructure.event.user

import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Confirmed(
    @Serializable(with = UUIDSerializer::class) val user: UUID
) : Event {
    override val key
        get() = Companion

    companion object : Event.Key<Confirmed>
}
