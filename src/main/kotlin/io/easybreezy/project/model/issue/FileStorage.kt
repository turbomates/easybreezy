package io.easybreezy.project.model.issue

import kotlinx.serialization.Serializable
import java.util.UUID

typealias Path = String

interface FileStorage {
    suspend fun add(file: File, issueId: UUID) : Path
    suspend fun get(issueId: UUID, fileName: Path) : java.io.File
    suspend fun remove(issueId: UUID, fileName: Path)
}

@Serializable
data class File(val name: String, val encodedContent: String, val extension: String)