package io.easybreezy.calendar.model

import java.util.Date
import java.util.UUID

typealias AbsenceId = UUID

class Absence(
    private val startedAt: Date,
    private val endedAt: Date,
    private val comment: String?,
    private val reason: Reason,
    private val userId: UUID,
    private val id: AbsenceId = UUID.randomUUID()
)

enum class Reason {
    VACATION, SICK, DAYON, PERSONAL
}
