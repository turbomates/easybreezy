@file:UseSerializers(UUIDSerializer::class)
package io.easybreezy.project.application.issue.command

import io.easybreezy.infrastructure.serialization.UUIDSerializer
import io.easybreezy.project.model.issue.Path
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.UUID

@Serializable
data class RemoveFile(val issueId: UUID, val path: Path)
