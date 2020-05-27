@file:UseSerializers(UUIDSerializer::class)
package io.easybreezy.infrastructure.event.event

import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.UUID

@Serializable
class ParticipantsAdded(
    val event: UUID,
    val participants: List<UUID>
) : Event {
    override val key
        get() = Companion

    companion object : Event.Key<ParticipantsAdded>
}
