package io.easybreezy.hr.infrastructure

import io.easybreezy.hr.model.absence.WorkingHour
import io.easybreezy.hr.model.absence.WorkingHours
import io.easybreezy.hr.model.exception.WorkingHourNotFoundException
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class WorkingHourRepository : WorkingHour.Repository() {

    fun getOne(id: UUID): WorkingHour {
        return find(id) ?: throw WorkingHourNotFoundException(id)
    }

    private fun find(id: UUID): WorkingHour? {
        return transaction {
            find { WorkingHours.id eq id }.firstOrNull()
        }
    }

    fun remove(ids: List<UUID>) {
        transaction {
            WorkingHours.deleteWhere { WorkingHours.id inList ids }
        }
    }
}