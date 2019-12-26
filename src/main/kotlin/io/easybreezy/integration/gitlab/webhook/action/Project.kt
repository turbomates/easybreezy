package io.easybreezy.integration.gitlab.webhook.action

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: Int,
    val name: String,
    val description: String,
    val webURL: String,
    val avatarURL: String,
    val gitSSHURL: String,
    val gitHTTPURL: String,
    val namespace: String,
    val visibilityLevel: Int,
    val pathWithNamespace: String,
    val defaultBranch: String,
    val homepage: String,
    val URL: String,
    val SSHURL: String,
    val HTTPURL: String
)