@file:UseSerializers(LocalDateSerializer::class)
package io.easybreezy.integration.gitlab.webhook.action

import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDate

@Serializable
data class Issue(
    val objectKind: ObjectKind,
    val user: User,
    val project: Project,
    val repository: Repository,
    val assignees: User,
    val assignee: User,
    val objectAttributes: ObjectAttributes,
    val labels: List<Label>,
    val changes: Changes
) {
    @Serializable
    data class ObjectAttributes(
        val id: Int,
        val title: String,
        val assigneeIds: List<Int>,
        val assigneeId: Int,
        val authorId: Int,
        val projectId: Int,
        val createdAt: LocalDate,
        val updatedAt: LocalDate,
        val position: Int,
        val branchName: String,
        val description: String,
        val milestoneId: Int,
        val State: String,
        val iid: Int,
        val URL: String,
        val action: String
    )
}
