package io.easybreezy.infrastructure.event.event

import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class EntryOpened(
    @Serializable(with = UUIDSerializer::class) val event: UUID
) : Event {
    override val key
        get() = Companion

    companion object : Event.Key<EntryOpened>
}
