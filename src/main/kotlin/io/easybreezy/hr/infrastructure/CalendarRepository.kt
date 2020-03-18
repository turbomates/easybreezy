package io.easybreezy.hr.infrastructure

import io.easybreezy.hr.model.absence.CalendarNotFoundException
import io.easybreezy.hr.model.calendar.Calendar
import io.easybreezy.hr.model.calendar.Calendars
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class CalendarRepository : Calendar.Repository() {

    fun getOne(id: UUID): Calendar {
        return find(id) ?: throw CalendarNotFoundException(id)
    }

    fun remove(id: UUID) {
        transaction {
            val calendar = find(id)
            calendar?.delete()
        }
    }

    private fun find(id: UUID): Calendar? {
        return transaction {
            find { Calendars.id eq id }.firstOrNull()
        }
    }
}
