package io.easybreezy.project.model.project

import io.easybreezy.project.model.team.Member
import java.time.LocalDateTime
import java.util.*

class Issue(
    private val project: UUID,
    private val title: String,
    private val description: String,
    private val author: Member,
    private val status: Status,
    private val level: LevelValue
) {
    private val id: UUID = UUID.randomUUID()
    private val comments: List<Comment> = emptyList()
    private val files: List<String> = emptyList()
    private val assignee: Member? = null
    private val createdAt: LocalDateTime = LocalDateTime.now()
    private val updatedAt: LocalDateTime = LocalDateTime.now()
}