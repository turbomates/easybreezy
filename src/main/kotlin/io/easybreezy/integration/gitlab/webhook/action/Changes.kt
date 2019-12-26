@file:UseSerializers(LocalDateSerializer::class)

package io.easybreezy.integration.gitlab.webhook.action

import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDate

@Serializable
data class Changes(
    val updatedById: List<String?>,
    val updatedAt: List<LocalDate>,
    val labels: Map<String, Label>
)