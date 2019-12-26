@file:UseSerializers(LocalDateSerializer::class)
package io.easybreezy.integration.gitlab.webhook.action

import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDate

@Serializable
data class Commit(
    val id: String,
    val message: String,
    val timestamp: LocalDate,
    val URL: String,
    val author: Author,
    val added: List<String?> = emptyList(),
    val modified: List<String?> = emptyList(),
    val removed: List<String?> = emptyList()
)