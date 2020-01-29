package io.easybreezy.integration.gitlab.webhook.action

import kotlinx.serialization.Serializable

@Serializable
data class Repository(
    val name: String,
    val url: String,
    val description: String,
    val homepage: String,
    val gitHTTPURL: String?,
    val gitSSHURL: String?,
    val visibilityLevel: Int?
)
