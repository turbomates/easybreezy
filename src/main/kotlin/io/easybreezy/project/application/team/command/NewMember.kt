package io.easybreezy.project.application.team.command

import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*

@Serializable
data class NewMember(
    @Serializable(with = UUIDSerializer::class)
    val user: UUID,
    @Serializable(with = UUIDSerializer::class)
    val role: UUID
) {
    @Transient
    lateinit var team: UUID
}
