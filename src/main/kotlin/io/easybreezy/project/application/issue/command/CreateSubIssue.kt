package io.easybreezy.project.application.issue.command

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
class CreateSubIssue(val content: String) {
    @Transient
    lateinit var issue: UUID
    @Transient
    lateinit var member: UUID
}
