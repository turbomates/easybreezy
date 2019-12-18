package io.easybreezy.integration.gitlab.webhook.action

import java.time.LocalDate

data class Label(
    val id: Int,
    val title: String,
    val color: String,
    val projectId: Int,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
    val template: Boolean,
    val description: String,
    val type: String,
    val groupId: Int
)