package io.easybreezy.integration.gitlab.webhook.action

data class User(
    val name: String,
    val username: String,
    val avatar: String//avatar_url
)