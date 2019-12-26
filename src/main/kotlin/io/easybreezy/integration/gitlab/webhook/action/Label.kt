@file:UseSerializers(LocalDateSerializer::class)
package io.easybreezy.integration.gitlab.webhook.action

import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDate

@Serializable
data class Label(
    val id: Int,
    val title: String,
    val color: String,
    val projectId: Int,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
    val template: Boolean,
    val description: String,
    val type: String,
    val groupId: Int
)