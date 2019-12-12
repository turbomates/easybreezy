package io.easybreezy.calendar.model

import java.util.Date
import java.util.UUID

class WorkingHour(
    private val id: UUID,
    private val day: Date,
    private val count: Byte,
    private val absenceId: AbsenceId
)