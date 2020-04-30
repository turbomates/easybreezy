package io.easybreezy.infrastructure.event.project.project

import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class SlugChanged(
    @Serializable(with = UUIDSerializer::class) val project: UUID,
    val slug: String
) : Event {
    override val key
        get() = Companion

    companion object : Event.Key<Activated>
}
