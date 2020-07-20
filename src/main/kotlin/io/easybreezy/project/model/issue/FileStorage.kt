package io.easybreezy.project.model.issue

import io.easybreezy.common.model.File
import kotlinx.serialization.Serializable
import java.util.UUID

interface FileStorage {
    suspend fun upload(file: UploadedAttachment, issueId: UUID): File
    suspend fun get(file: File): java.io.File
    suspend fun remove(file: File)
}

@Serializable
data class UploadedAttachment(val name: String, val encodedContent: String, val extension: String)
