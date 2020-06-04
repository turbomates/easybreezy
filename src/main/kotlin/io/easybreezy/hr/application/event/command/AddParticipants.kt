@file:UseSerializers(UUIDSerializer::class)
package io.easybreezy.hr.application.event.command

import io.easybreezy.hr.model.event.EventId
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import java.util.UUID

@Serializable
data class AddParticipants(val participants: List<UUID>) {
    @Transient
    lateinit var event: EventId
}
