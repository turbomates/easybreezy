package io.easybreezy.integration.gitlab.webhook.action

import kotlinx.serialization.Serializable

@Serializable
data class WikiPage(
    val objectKind: ObjectKind,
    val user: User,
    val project: Project,
    val wiki: Wiki,
    val objectAttributes: ObjectAttributes

) {
    @Serializable
    data class Wiki(
        val webURL: String,
        val gitSSHURL: String,
        val gitHTTPURL: String,
        val pathWithNamespace: String,
        val defaultBranch: String
    )

    @Serializable
    data class ObjectAttributes(
        val title: String,
        val content: String,
        val format: String,
        val message: String,
        val slug: String,
        val url: String,
        val action: String
    )
}
