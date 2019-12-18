package io.easybreezy.integration.gitlab.webhook.action

data class Repository(
    val name: String,
    val url: String,
    val description: String,
    val homepage: String,
    val gitHTTPURL: String?,
    val gitSSHURL: String?,
    val visibilityLevel: Int?
)