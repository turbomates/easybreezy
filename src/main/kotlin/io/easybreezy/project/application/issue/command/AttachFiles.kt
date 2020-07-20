package io.easybreezy.project.application.issue.command

import io.easybreezy.project.model.issue.UploadedAttachment
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
data class AttachFiles(val files: List<UploadedAttachment>) {
    @Transient
    lateinit var issueId: UUID
}
