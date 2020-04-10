package io.easybreezy.hr.infrastructure

import io.easybreezy.hr.model.absence.WorkingHour
import io.easybreezy.hr.model.absence.WorkingHourNotFoundException
import io.easybreezy.hr.model.absence.WorkingHours
import java.util.UUID

class WorkingHourRepository : WorkingHour.Repository() {

    fun getOne(id: UUID): WorkingHour {
        return find(id) ?: throw WorkingHourNotFoundException(id)
    }

    private fun find(id: UUID): WorkingHour? {
        return find { WorkingHours.id eq id }.firstOrNull()
    }
}
