package io.easybreezy.integration.gitlab.webhook.action

import kotlinx.serialization.Serializable

@Serializable
data class Author(
    val name: String,
    val email: String
)
