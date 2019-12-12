package io.easybreezy.calendar.infrastructure

import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.date

object WorkingHours : UUIDTable("working_hours") {
    val day = date("day")
    val count = integer("count")
    val absenceId = reference("absence_id", Absences)
}