package io.easybreezy.calendar.model

import io.easybreezy.user.model_legacy.UserId
import java.util.Date
import java.util.UUID

typealias AbsenceId = UUID

class Absence(
    private val id: AbsenceId,
    private val startedAt: Date,
    private val endedAt: Date,
    private val comment: String,
    private val type: Reason,
    private val userId: UserId
)

enum class Reason {
    VACATION, SICK, DAYON, PERSONAL
}