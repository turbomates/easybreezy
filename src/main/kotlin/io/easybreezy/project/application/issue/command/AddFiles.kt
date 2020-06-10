package io.easybreezy.project.application.issue.command

import io.easybreezy.project.model.issue.File
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
data class AddFiles(val files: List<File>) {
    @Transient
    lateinit var issueId: UUID
}