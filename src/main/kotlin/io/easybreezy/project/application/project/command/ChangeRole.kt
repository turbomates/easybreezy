package io.easybreezy.project.application.project.command

import io.easybreezy.project.model.team.Role
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
data class ChangeRole(
    val name: String? = null,
    val permissions: List<Role.Permission>
) {
    @Transient
    lateinit var roleId: UUID
    @Transient
    lateinit var project: String
}
