package io.easybreezy.project.application.issue

import io.easybreezy.infrastructure.upload.LocalFileManager
import io.easybreezy.project.model.issue.File
import io.easybreezy.project.model.issue.Path
import java.util.UUID
import io.easybreezy.project.model.issue.FileStorage as FileStorageInterface

private const val BUCKET = "issues-files"

class FileStorage : FileStorageInterface, LocalFileManager() {

    override suspend fun add(file: File, issueId: UUID): Path {
        return add(
            file.encodedContent,
            "$BUCKET/$issueId",
            file.extension,
            file.name
        )
    }

    override suspend fun get(issueId: UUID, fileName: Path): java.io.File {
        return get("$BUCKET/$issueId/$fileName")
    }

    override suspend fun remove(issueId: UUID, fileName: Path) {
        return remove("$BUCKET/$issueId/$fileName")
    }
}
