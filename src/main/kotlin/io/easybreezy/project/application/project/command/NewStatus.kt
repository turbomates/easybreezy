package io.easybreezy.project.application.project.command

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class NewStatus(
    val name: String
) {
    @Transient
    lateinit var project: String
}
