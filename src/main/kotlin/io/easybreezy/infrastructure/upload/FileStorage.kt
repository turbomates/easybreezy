package io.easybreezy.infrastructure.upload

import io.easybreezy.common.model.File
import kotlinx.serialization.Serializable

interface FileStorage {
    suspend fun upload(file: UploadedFile, directory: String): File
    suspend fun get(file: File): java.io.File
    suspend fun remove(file: File)
}

@Serializable
data class UploadedFile(val name: String, val encodedContent: String, val extension: String)
