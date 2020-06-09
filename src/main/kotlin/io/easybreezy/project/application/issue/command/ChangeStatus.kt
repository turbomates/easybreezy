package io.easybreezy.project.application.issue.command

import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
class ChangeStatus(@Serializable(with = UUIDSerializer::class) val newStatus: UUID) {
    @Transient
    lateinit var issue: UUID
    @Transient
    lateinit var project: String
}
