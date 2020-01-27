package io.easybreezy.integration.inner.message

import kotlinx.serialization.Serializable

@Serializable
data class Reference(
    val type: String,
    val assignee: Author?,
    val title: String,
    val sate: String? = null
)
