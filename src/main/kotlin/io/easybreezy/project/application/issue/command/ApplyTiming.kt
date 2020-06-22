package io.easybreezy.project.application.issue.command

import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class ApplyTiming(@Serializable(with = UUIDSerializer::class) var issue: UUID, var description: String)
