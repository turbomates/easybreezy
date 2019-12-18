package io.easybreezy.calendar.infrastructure

import io.easybreezy.calendar.model.Reason
import io.easybreezy.user.model.Users
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.date

object Absences : UUIDTable("absences") {
    val startedAt = date("from")
    val endedAt = date("to")
    val comment = text("comment")
    val reason = enumerationByName("reason", 10, Reason::class)
    val userId = reference("user_id", Users)
}