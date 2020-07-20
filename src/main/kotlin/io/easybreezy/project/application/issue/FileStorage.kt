package io.easybreezy.project.application.issue

import com.google.inject.Inject
import io.easybreezy.common.model.File
import io.easybreezy.common.model.FileLocation
import io.easybreezy.infrastructure.upload.LocalFileManager
import io.easybreezy.project.model.issue.UploadedAttachment
import java.util.UUID
import io.easybreezy.project.model.issue.FileStorage as FileStorageInterface

private const val BUCKET = "issues-files"

class FileStorage @Inject constructor(private val manager: LocalFileManager) : FileStorageInterface {

    override suspend fun upload(file: UploadedAttachment, issueId: UUID): File {
        val path = manager.add(
            file.encodedContent,
            "$BUCKET/$issueId",
            file.extension,
            file.name
        )
        return File.add(file.name, file.extension, FileLocation.create("$BUCKET/$issueId/$path"))
    }

    override suspend fun get(file: File): java.io.File {
        return manager.get(file.location.path)
    }

    override suspend fun remove(file: File) {
        return manager.remove(file.location.path)
    }
}
