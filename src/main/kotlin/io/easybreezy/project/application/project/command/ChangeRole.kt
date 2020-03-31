package io.easybreezy.project.application.project.command

import kotlinx.serialization.Serializable

@Serializable
data class ChangeRole(
    val name: String? = null,
    val permissions: List<String>
)
