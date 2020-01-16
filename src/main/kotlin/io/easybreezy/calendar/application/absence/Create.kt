package io.easybreezy.calendar.application.absence

import java.util.Date
import java.util.UUID

data class Create(
    val startedAt: Date,
    val endedAt: Date,
    val reason: String,
    val userId: UUID,
    val comment: String?
)
