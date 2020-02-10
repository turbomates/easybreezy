package io.easybreezy.hr.application.absence.queryobject

import io.easybreezy.hr.model.absence.WorkingHours
import io.easybreezy.infrastructure.exposed.toUUID
import io.easybreezy.infrastructure.query.ContinuousList
import io.easybreezy.infrastructure.query.PagingParameters
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.query.toContinuousList
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class WorkingHourQO(private val workingHourId: UUID) : QueryObject<WorkingHour> {
    override suspend fun getData() =
        transaction {
            WorkingHours.select {
                WorkingHours.id eq workingHourId
            }.first().toWorkingHour()
        }
}

class WorkingHoursQO(private val userId: UUID, private val paging: PagingParameters) : QueryObject<ContinuousList<WorkingHour>> {
    override suspend fun getData() =
        transaction {
            WorkingHours
                .selectAll()
                .andWhere { WorkingHours.userId eq userId }
                .limit(paging.pageSize, paging.offset)
                .map { it.toWorkingHour() }
                .toContinuousList(paging.pageSize, paging.currentPage)
        }
}

private fun ResultRow.toWorkingHour() = WorkingHour(
    id = this[WorkingHours.id].toUUID(),
    day = this[WorkingHours.day].toString(),
    count = this[WorkingHours.count],
    userId = this[WorkingHours.userId]
)

data class WorkingHour(
    val id: UUID,
    val day: String,
    val count: Int,
    val userId: UUID
)
