package io.easybreezy.hr.infrastructure

import io.easybreezy.hr.model.absence.Absence
import io.easybreezy.hr.model.absence.AbsenceNotFoundException
import io.easybreezy.hr.model.absence.Absences
import java.util.UUID

class AbsenceRepository : Absence.Repository() {
    fun getOne(id: UUID): Absence {
        return find(id) ?: throw AbsenceNotFoundException(id)
    }

    private fun find(id: UUID): Absence? {
        return find { Absences.id eq id }.firstOrNull()
    }

    fun remove(id: UUID) {
        val absence = find(id)
        absence?.delete()
    }
}
