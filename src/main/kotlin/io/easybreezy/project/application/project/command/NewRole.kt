package io.easybreezy.project.application.project.command

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class NewRole(
    val name: String,
    val permissions: List<String>
) {
    @Transient
    lateinit var project: String
}
