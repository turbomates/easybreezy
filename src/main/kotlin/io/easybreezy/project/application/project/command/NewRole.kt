package io.easybreezy.project.application.project.command

import kotlinx.serialization.Serializable

@Serializable
data class NewRole(
    val name: String,
    val permissions: List<String>
)
