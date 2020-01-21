package io.easybreezy.hr.application.absence

import java.util.Date

data class Update(
    val startedAt: Date,
    val endedAt: Date,
    val comment: String,
    val reason: String
)