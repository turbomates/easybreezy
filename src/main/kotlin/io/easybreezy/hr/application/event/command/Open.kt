@file:UseSerializers(UUIDSerializer::class, LocalDateTimeSerializer::class)
package io.easybreezy.hr.application.event.command

import io.easybreezy.hr.model.event.Owner
import io.easybreezy.infrastructure.serialization.LocalDateTimeSerializer
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class Open(
    val name: String,
    val startedAt: LocalDateTime,
    val description: String? = null,
    val endedAt: LocalDateTime? = null,
    val location: UUID? = null,
    val participants: List<UUID>? = null,
    val isPrivate: Boolean = false,
    val isFreeEntry: Boolean = false,
    val isRepeatable: Boolean = false,
    val days: Set<String>? = null
) {
    @Transient
    lateinit var owner: Owner
}
