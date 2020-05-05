package io.easybreezy.project.application.project.command

import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
data class ChangeCategory(
    val name: String? = null,
    @Serializable(with = UUIDSerializer::class)
    val parent: UUID? = null
) {
    @Transient
    lateinit var categoryId: UUID
    @Transient
    lateinit var project: String
}
