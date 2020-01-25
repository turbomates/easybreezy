package io.easybreezy.hr.infrastructure

import io.easybreezy.hr.model.absence.Absence
import io.easybreezy.hr.model.absence.Absences
import io.easybreezy.hr.model.exception.AbsenceNotFoundException
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class AbsenceRepository : Absence.Repository() {
    fun getOne(id: UUID): Absence {
        return find(id) ?: throw AbsenceNotFoundException(id)
    }

    private fun find(id: UUID): Absence? {
        return transaction {
            find { Absences.id eq id }.firstOrNull()
        }
    }

    fun remove(id: UUID) {
        transaction {
            val absence = find(id)
            absence?.delete()
        }
    }
}