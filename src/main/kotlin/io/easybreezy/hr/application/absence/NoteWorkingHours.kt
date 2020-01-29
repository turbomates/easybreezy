package io.easybreezy.hr.application.absence

import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class NoteWorkingHours(
    val workingHours: List<WorkingHour>,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID
)
