package io.easybreezy.project.application.project.command

import io.easybreezy.project.model.team.Role
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class NewRole(
    val name: String,
    val permissions: List<Role.Permission>
) {
    @Transient
    lateinit var project: String
}
