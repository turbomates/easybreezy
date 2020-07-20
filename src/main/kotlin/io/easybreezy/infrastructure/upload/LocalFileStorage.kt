package io.easybreezy.infrastructure.upload

import com.google.inject.Inject
import io.easybreezy.common.model.File
import io.easybreezy.common.model.FileLocation

class LocalFileStorage @Inject constructor(private val manager: LocalFileManager) : FileStorage {

    override suspend fun upload(file: UploadedFile, directory: String): File {
        val path = manager.add(
            file.encodedContent,
            directory,
            file.extension,
            file.name
        )
        return File.add(file.name, file.extension, FileLocation.create("$directory/$path"))
    }

    override suspend fun get(file: File): java.io.File {
        return manager.get(file.location.path)
    }

    override suspend fun remove(file: File) {
        return manager.remove(file.location.path)
    }
}
