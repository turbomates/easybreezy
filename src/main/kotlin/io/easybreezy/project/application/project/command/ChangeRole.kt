package io.easybreezy.project.application.project.command

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
data class ChangeRole(
    val name: String? = null,
    val permissions: List<String>
) {
    @Transient
    lateinit var roleId: UUID
    @Transient
    lateinit var project: String
}
