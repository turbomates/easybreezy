package io.easybreezy.hr.application.absence.queryobject

import io.easybreezy.hr.model.absence.Absences
import io.easybreezy.hr.model.absence.Reason
import io.easybreezy.infrastructure.exposed.toUUID
import io.easybreezy.infrastructure.query.ContinuousList
import io.easybreezy.infrastructure.query.PagingParameters
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.query.toContinuousList
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
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

class AbsencesQO(private val userId: UUID, private val paging: PagingParameters) :
    QueryObject<ContinuousList<Absence>> {
    override fun getData() =
        transaction {
            Absences
                .selectAll()
                .andWhere { Absences.userId eq userId }
                .limit(paging.pageSize, paging.offset)
                .map { it.toAbsence() }
                .toContinuousList(paging.pageSize, paging.currentPage)
        }
}

private fun ResultRow.toAbsence() = Absence(
    id = this[Absences.id].toUUID(),
    startedAt = this[Absences.startedAt].toString(),
    endedAt = this[Absences.endedAt].toString(),
    comment = this[Absences.comment],
    reason = this[Absences.reason],
    userId = this[Absences.userId]
)

@Serializable
data class Absence(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val startedAt: String,
    val endedAt: String,
    val comment: String?,
    val reason: Reason,
    @Serializable(with = UUIDSerializer::class) val userId: UUID
)
