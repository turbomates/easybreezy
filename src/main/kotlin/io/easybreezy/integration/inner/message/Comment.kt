package io.easybreezy.integration.inner.message

import kotlinx.serialization.Serializable

sealed class Comment() {
}

@Serializable
data class IssueComment(
    val vsc: String,
    val note: String,
    val url: String,
    val author: Author,
    val issue: Issue,
    val project: Project
) : Comment()

@Serializable
data class ReviewComment(
    val vsc: String,
    val note: String,
    val url: String,
    val author: Author,
    val review: Reference,
    val project: Project
) : Comment()
