package io.easybreezy.infrastructure.event.project.project

import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class NewTeamAdded(
    @Serializable(with = UUIDSerializer::class) val project: UUID,
    @Serializable(with = UUIDSerializer::class) val team: UUID,
    val name: String
) : Event {
    override val key
        get() = Companion

    companion object : Event.Key<NewTeamAdded>
}
