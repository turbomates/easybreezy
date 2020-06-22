@file:UseSerializers(UUIDSerializer::class)
package io.easybreezy.project.application.issue.command

import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.UUID

@Serializable
class ApplyWorkflow(var issue: UUID, var project: UUID)
