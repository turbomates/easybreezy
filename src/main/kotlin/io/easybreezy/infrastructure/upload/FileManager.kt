package io.easybreezy.infrastructure.upload

import java.io.File

typealias Path = String

interface FileManager<in T> {
    suspend fun add(content: T, bucket: String, extension: String, fileName: String?) : Path
    suspend fun get(path: Path): File
    suspend fun remove(path: String)
}