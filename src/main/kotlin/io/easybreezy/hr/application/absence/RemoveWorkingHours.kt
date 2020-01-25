package io.easybreezy.hr.application.absence

import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class RemoveWorkingHours(val workingHours: List<@Serializable(with = UUIDSerializer::class) UUID>)