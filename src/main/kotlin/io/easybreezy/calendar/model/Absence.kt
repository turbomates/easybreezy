package io.easybreezy.calendar.model

import io.easybreezy.infrastructure.domain.AggregateRoot
import io.easybreezy.user.model_legacy.UserId
import java.util.Date
import java.util.UUID

typealias AbsenceId = UUID

class Absence(
    private val startedAt: Date,
    private val endedAt: Date,
    private val comment: String,
    private val reason: Reason,
    private val userId: UserId,
    private val id: AbsenceId = UUID.randomUUID()
) : AggregateRoot()

enum class Reason {
    VACATION, SICK, DAYON, PERSONAL
}