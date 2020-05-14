package io.easybreezy.hr.application.absence.queryobject

import io.easybreezy.hr.model.absence.Absences as AbsencesTable
import io.easybreezy.hr.model.absence.Reason
import io.easybreezy.infrastructure.query.DateRange
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import java.util.UUID

class AbsenceQO(private val absenceId: UUID) : QueryObject<Absence> {
    override suspend fun getData(): Absence {
        return AbsencesTable.select {
            AbsencesTable.id eq absenceId
        }.first().toAbsence()
    }
}

class AbsencesQO(private val dateRange: DateRange) : QueryObject<Absences> {
    override suspend fun getData(): Absences {
        return AbsencesTable
            .selectAll()
            .andWhere { AbsencesTable.startedAt greater dateRange.from }
            .andWhere { AbsencesTable.endedAt less dateRange.to }
            .toAbsences()
    }
}

class IsAbsenceOwner(val id: UUID, val userId: UUID) : QueryObject<Boolean> {
    override suspend fun getData(): Boolean {
        return AbsencesTable.select { AbsencesTable.id eq id and (AbsencesTable.userId eq userId) }.count() > 0
    }
}

class UserAbsencesQO(private val userId: UUID) : QueryObject<UserAbsences> {
    override suspend fun getData(): UserAbsences {
        val absences = AbsencesTable
            .selectAll()
            .andWhere { AbsencesTable.userId eq userId }
            .map { it.toAbsence() }

        return UserAbsences(absences)
    }
}

private fun Iterable<ResultRow>.toAbsences(): Absences {
    val data = fold(mutableMapOf<UUID, MutableList<Absence>>()) { map, resultRaw ->
        val absence = resultRaw.toAbsence()
        if (map.containsKey(absence.userId)) map[absence.userId]?.add(absence) else map[absence.userId] =
            mutableListOf(absence)
        map
    }

    return Absences(data)
}

private fun ResultRow.toAbsence() = Absence(
    id = this[AbsencesTable.id].value,
    startedAt = this[AbsencesTable.startedAt].toString(),
    endedAt = this[AbsencesTable.endedAt].toString(),
    comment = this[AbsencesTable.comment],
    reason = this[AbsencesTable.reason],
    userId = this[AbsencesTable.userId],
    isApproved = this[AbsencesTable.isApproved]
)

@Serializable
data class Absence(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val startedAt: String,
    val endedAt: String,
    val comment: String?,
    val reason: Reason,
    @Serializable(with = UUIDSerializer::class) val userId: UUID,
    val isApproved: Boolean
)

@Serializable
data class UserAbsences(
    val absences: List<Absence>
)

@Serializable
data class Absences(
    val absences: Map<@Serializable(with = UUIDSerializer::class) UUID, List<Absence>>
)
