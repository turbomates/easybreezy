package io.easybreezy.project.application.team.command

import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
data class ChangeMemberRole(
    @Serializable(with = UUIDSerializer::class)
    val newRoleId: UUID
) {
    @Transient
    lateinit var memberId: UUID
    @Transient
    lateinit var team: UUID
}
