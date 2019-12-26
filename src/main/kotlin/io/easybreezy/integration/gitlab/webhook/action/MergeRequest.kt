@file:UseSerializers(LocalDateSerializer::class)
package io.easybreezy.integration.gitlab.webhook.action

import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDate

@Serializable
data class MergeRequest(
    val objectKind: ObjectKind,
    val user: User,
    val project: Project,
    val repository: Repository,
    val objectAttributes: ObjectAttributes
) {
    @Serializable
    data class ObjectAttributes(
        val id: Int,
        val targetBranch: String,
        val sourceBranch: String,
        val sourceProjectId: Int,
        val authorId: Int,
        val assigneeId: Int,
        val title: String,
        val updatedAt: LocalDate,
        val createdAt: LocalDate,
        val milestoneId: Int?,
        val state: String,
        val mergeStatus: String,
        val targetProjectId: Int,
        val iid: Int,
        val description: String,
        val source: Description,
        val target: Description,
        val lastCommit: Commit,
        val workInProgress: Boolean,
        val URL: String,
        val action: String,
        val assignee: User,
        val labels: List<Label>,
        val changes: Changes
    ) {
        @Serializable
        data class Description(
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
            val url: String,
            val SHHURL: String,
            val HTTPURL: String
        )
    }
}