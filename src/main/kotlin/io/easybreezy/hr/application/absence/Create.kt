package io.easybreezy.hr.application.absence

import java.time.LocalDate
import java.util.UUID

data class Create(
    val startedAt: LocalDate,
    val endedAt: LocalDate,
    val reason: String,
    val userId: UUID,
    val comment: String?
)
