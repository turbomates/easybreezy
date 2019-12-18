package io.easybreezy.calendar.model

import io.easybreezy.user.model_legacy.UserId
import java.util.Date
import java.util.UUID

class WorkingHour(
    private val id: UUID,
    private val day: Date,
    private val count: Byte,
    private val userId: UserId
)