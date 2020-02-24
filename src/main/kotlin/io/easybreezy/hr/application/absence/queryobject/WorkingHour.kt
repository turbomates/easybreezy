package io.easybreezy.hr.application.absence.queryobject

import io.easybreezy.hr.model.absence.WorkingHours as WorkingHoursTable
import io.easybreezy.infrastructure.exposed.toUUID
import io.easybreezy.infrastructure.query.DateRange
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class WorkingHourQO(private val workingHourId: UUID) : QueryObject<WorkingHour> {
    override suspend fun getData() =
        transaction {
            WorkingHoursTable.select {
                WorkingHoursTable.id eq workingHourId
            }.first().toWorkingHour()
        }
}

class WorkingHoursQO(private val dateRange: DateRange) : QueryObject<WorkingHours> {
    override suspend fun getData(): WorkingHours {
        return WorkingHoursTable
            .selectAll()
            .andWhere { WorkingHoursTable.day greater dateRange.from }
            .andWhere { WorkingHoursTable.day less dateRange.to }
            .toWorkingHours()
    }
}

class UserWorkingHoursQO(private val userId: UUID) : QueryObject<UserWorkingHours> {
    override suspend fun getData() =
        transaction {
            val workingHours = WorkingHoursTable
                .selectAll()
                .andWhere { WorkingHoursTable.userId eq userId }
                .map { it.toWorkingHour() }

            UserWorkingHours(workingHours)
        }
}

private fun Iterable<ResultRow>.toWorkingHours(): WorkingHours {
    val data = fold(mutableMapOf<UUID, MutableList<WorkingHour>>()) { map, resultRaw ->
        val workingHour = resultRaw.toWorkingHour()
        if (map.containsKey(workingHour.userId))
            map[workingHour.userId]?.add(workingHour)
        else map[workingHour.userId] = mutableListOf(workingHour)
        map
    }

    return WorkingHours(data)
}

private fun ResultRow.toWorkingHour() = WorkingHour(
    id = this[WorkingHoursTable.id].toUUID(),
    day = this[WorkingHoursTable.day].toString(),
    count = this[WorkingHoursTable.count],
    userId = this[WorkingHoursTable.userId]
)

@Serializable
data class WorkingHour(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val day: String,
    val count: Int,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID
)

@Serializable
data class UserWorkingHours(
    val workingHours: List<WorkingHour>
)

@Serializable
data class WorkingHours(
    val workingHours: Map<@Serializable(with = UUIDSerializer::class) UUID, List<WorkingHour>>
)
