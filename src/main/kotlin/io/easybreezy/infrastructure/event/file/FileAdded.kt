package io.easybreezy.infrastructure.event.file

import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class FileAdded(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val path: String
) : Event {
    override val key
        get() = Companion

    companion object : Event.Key<FileAdded>
}
