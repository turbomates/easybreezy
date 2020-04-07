package io.easybreezy.infrastructure.event.project.team

import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.serialization.LocalDateTimeSerializer
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class MemberAdded(
    @Serializable(with = UUIDSerializer::class) val team: UUID,
    @Serializable(with = UUIDSerializer::class) val member: UUID,
    @Serializable(with = LocalDateTimeSerializer::class) val at: LocalDateTime
) : Event {
    override val key
        get() = Companion

    companion object : Event.Key<MemberAdded>
}
