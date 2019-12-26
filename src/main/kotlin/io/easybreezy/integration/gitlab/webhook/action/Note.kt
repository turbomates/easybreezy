@file:UseSerializers(LocalDateSerializer::class)
package io.easybreezy.integration.gitlab.webhook.action

import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDate

@Serializable
data class Note(
    val objectKind: ObjectKind,
    val user: User,
    val projectId: Int,
    val project: Project,
    val repository: Repository,
    val commits: Commit,
    val mergeRequest: MergeRequest?,
    val issue: Issue?,
    val snippet: Snippet?,
    val objectAttributes: ObjectAttributes,
    val workInProgress: Boolean?,
    val assignee: Author?

) {
    @Serializable
    data class ObjectAttributes(
        val id: Int,
        val note: String,
        val noteableType: String,
        val authorId: Int,
        val updatedAt: LocalDate,
        val createdAt: LocalDate,
        val projectId: Int,
        val lineCode: String,
        val commitId: String,
        val noteableId: String,
        val system: Boolean,
        val stDiff: SfDiff?,
        val url: String
    )

    @Serializable
    data class SfDiff(
        val diff: String,
        val newPath: String,
        val oldPath: String,
        val aMode: String,
        val bMode: String,
        val newFile: Boolean,
        val renamedFile: Boolean,
        val deletedFile: Boolean
    )

    @Serializable
    data class Snippet(
        val id: Int,
        val title: String,
        val content: String,
        val authorId: Int,
        val projectId: Int,
        val createdAt: LocalDate,
        val updatedAt: LocalDate,
        val fileName: String,
        val expiresAt: LocalDate,
        val type: String,
        val visibilityLevel: Int
    )

    @Serializable
    data class Issue(
        val id: Int,
        val title: String,
        val assigneeIds: List<Int>,
        val assigneeId: Int,
        val authorId: Int,
        val projectId: Int,
        val createdAt: LocalDate,
        val updatedAt: LocalDate,
        val position: Int,
        val branchName: String?,
        val description: String?,
        val milestoneId: Int?,
        val state: String,
        val iid: Int
    )

    @Serializable
    data class MergeRequest(
        val id: Int,
        val targetBranch: String,
        val sourceBranch: String,
        val sourceProjectId: Int,
        val authorId: Int,
        val assigneeId: Int,
        val title: String,
        val createdAt: LocalDate,
        val updatedAt: LocalDate,
        val milestoneId: Int,
        val state: String,
        val mergeStatus: String,
        val targetProjectId: Int,
        val iid: Int,
        val description: String,
        val position: Int,
        val source: Description,
        val target: Description,
        val lastCommit: Commit

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