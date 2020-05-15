package io.easybreezy.project.application.team.command

import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class NewTeam(
    val name: String,
    @Serializable(with = UUIDSerializer::class)
    val project: UUID
)
