package io.easybreezy.integration.gitlab.webhook.action

import kotlinx.serialization.Serializable

@Serializable
data class Push(
    val objectKind: String,
    val before: String,
    val after: String,
    val ref: String,
    val checkoutSha: String,
    val userId: Int,
    val userName: String,
    val userUsername: String,
    val userEmail: String,
    val userAvatar: String,
    val projectId: Int,
    val project: Project,
    val repository: Repository,
    val commits: List<Commit>,
    val totalCommitsCount: Int
)
