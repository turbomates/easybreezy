package io.easybreezy.hr.model.absence

import io.easybreezy.user.model.Users
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.date
import java.util.Date
import java.util.UUID

object WorkingHours : UUIDTable("working_hours") {
    val day = date("day")
    val count = integer("count")
    val userId = Absences.reference("user_id", Users)
}

class WorkingHour(
    private val id: UUID,
    private val day: Date,
    private val count: Byte,
    private val userId: UUID
)
