package io.easybreezy.project.application.issue.command

import io.easybreezy.infrastructure.upload.UploadedFile
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
data class AttachFiles(val files: List<UploadedFile>) {
    @Transient
    lateinit var issueId: UUID
}
