@file:UseSerializers(UUIDSerializer::class)
package io.easybreezy.infrastructure.event.project.project

import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.serialization.LocalDateTimeSerializer
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import io.easybreezy.project.model.team.Role
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class RoleAdded(
    val project: UUID,
    val role: UUID,
    val name: String,
    val permissions: List<Role.Permission>,
    @Serializable(with = LocalDateTimeSerializer::class) val at: LocalDateTime
) : Event {
    override val key
        get() = Companion

    companion object : Event.Key<RoleAdded>
}
