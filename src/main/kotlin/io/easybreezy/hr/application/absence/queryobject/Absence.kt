package io.easybreezy.hr.application.absence.queryobject

import io.easybreezy.hr.model.absence.Absences
import io.easybreezy.hr.model.absence.Reason
import io.easybreezy.infrastructure.query.QueryObject
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class AbsenceQO(private val absenceId: UUID) : QueryObject<Absence> {
    override fun getData() =
        transaction {
            Absences.select {
                Absences.id eq absenceId
            }.first().toAbsence()
        }
}

//todo: paging
class AbsencesQO(private val userId: UUID) : QueryObject<List<Absence>> {
    override fun getData() =
        transaction {
            Absences.selectAll().andWhere { Absences.userId eq userId }.map { it.toAbsence() }
        }
}

private fun ResultRow.toAbsence() = Absence(
    id = UUID.fromString(this[Absences.id].toString()),
    startedAt = this[Absences.startedAt].toString(),
    endedAt = this[Absences.endedAt].toString(),
    comment = this[Absences.comment],
    reason = this[Absences.reason],
    userId = UUID.fromString(this[Absences.userId].toString())
)

data class Absence(
    val id: UUID,
    val startedAt: String,
    val endedAt: String,
    val comment: String?,
    val reason: Reason,
    val userId: UUID
)