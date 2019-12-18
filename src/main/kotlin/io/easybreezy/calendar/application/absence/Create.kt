package io.easybreezy.calendar.application.absence

import io.easybreezy.user.model_legacy.UserId
import java.util.Date

data class Create(
    val startedAt: Date,
    val endedAt: Date,
    val reason: String,
    val userId: UserId,
    val comment: String?
)