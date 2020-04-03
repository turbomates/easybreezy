package io.easybreezy.hr.model.vacation

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.date

object Vacations: Table("vacations") {
    val userId = uuid("user_id")
    val absenceHours = integer("absence_hours").nullable()
    val absenceDays = integer("absence_days").nullable()
    val locationStartedAt = date("location_started_at")
    val locationEndedAt = date("location_ended_at")
    val extraVacationDays = integer("extra_vacation_days").nullable()
    val vacationDaysPerYear = integer("vacation_days")
}