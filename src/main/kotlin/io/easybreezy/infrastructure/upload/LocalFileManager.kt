package io.easybreezy.infrastructure.upload

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Base64
import java.util.UUID

typealias EncodedContent = String
private const val BASE_UPLOADS_DIR = "uploads"

open class LocalFileManager : FileManager<EncodedContent> {
    override suspend fun add(
        content: EncodedContent,
        bucket: String,
        extension: String,
        fileName: String?
    ): Path = withContext(Dispatchers.IO) {
        val name = fileName ?: UUID.randomUUID().toString()
        val dir = "$BASE_UPLOADS_DIR/$bucket"

        val file = File(dir)
        if (!file.exists()) file.mkdirs()

        val fullFileName = "$name.$extension"
        val path = "$dir/$fullFileName"
        File(path).writeBytes(Base64.getDecoder().decode(content))

        fullFileName
    }

    override suspend fun get(path: Path): File {
        val file = File("$BASE_UPLOADS_DIR/$path")
        if (!file.exists()) throw NoSuchElementException("File not found")

        return file
    }

    override suspend fun remove(path: String) {
        val file = get(path)
        file.delete()
    }
}
