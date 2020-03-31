package io.easybreezy.hr.application

import io.easybreezy.hr.model.vacation.Vacations
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.UUID

class VacationQO(private val userId: UUID) : QueryObject<RemainingTime> {
    override suspend fun getData(): RemainingTime {
        return Vacations
            .selectAll()
            .andWhere { Vacations.userId eq userId }
            .map { it.toVacation() }
            .reduce(userId)
    }
}

class VacationsQO() : QueryObject<RemainingTimes> {
    override suspend fun getData(): RemainingTimes {
        val result = Vacations
            .selectAll()
            .map { it.toVacation() }
            .groupBy { it.userId }
            .mapValues { it.value.reduce(it.key) }

        return RemainingTimes(result)
    }
}

private fun ResultRow.toVacation() = Vacation(
    this[Vacations.userId],
    this[Vacations.absenceHours] ?: 0,
    this[Vacations.absenceDays] ?: 0,
    this[Vacations.locationStartedAt],
    this[Vacations.locationEndedAt],
    this[Vacations.extraVacationDays] ?: 0,
    this[Vacations.vacationDaysPerYear]
)

class Vacation(
    val userId: UUID,
    private val absenceHours: Int,
    private val absenceDays: Int,
    private val locationStartedAt: LocalDate,
    private val locationEndedAt: LocalDate,
    private val extraVacationDays: Int,
    private val vacationDaysPerYear: Int
) {
    fun calculateRemainingTime(): RemainingTime {
        val dayPerMonth = vacationDaysPerYear / 12
        val remains = vacationDaysPerYear % 12

        var lastWorkingDay = locationEndedAt
        if (locationEndedAt > LocalDate.now())  lastWorkingDay = LocalDate.now()

        val workedMonths = ChronoUnit.MONTHS.between(locationStartedAt, lastWorkingDay).toInt()
        var earnedDays = workedMonths * dayPerMonth + remains

        var hours = 0
        if (absenceHours > 0) {
            val hoursInDays = absenceHours / 8
            val hoursRemains = absenceHours % 8
            when {
                hoursInDays >= 1 && hoursRemains > 0 -> earnedDays -= hoursInDays + 1
                hoursInDays >= 1 && hoursRemains == 0 -> earnedDays -= hoursInDays
                else -> earnedDays--
            }
            hours = 8 - absenceHours % 8
        }

        return RemainingTime(
            userId,
            earnedDays + extraVacationDays - absenceDays,
            hours
        )

    }
}

@Serializable
class RemainingTime(
    @Serializable(with = UUIDSerializer::class)
    private val userId: UUID,
    private val days: Int,
    private val hours: Int
) {
    operator fun plus(other: RemainingTime): RemainingTime {
        if (userId != other.userId) throw IllegalArgumentException("Different users")
        var days = days + other.days
        var hours = hours + other.hours
        if (hours > 8) {
            days += hours / 8
            hours %= 8
        }

        return RemainingTime(userId, days, hours)
    }
}

@Serializable
data class RemainingTimes(val remainingTimes: Map<@Serializable(with = UUIDSerializer::class) UUID, RemainingTime>)

fun List<Vacation>.reduce(userId: UUID): RemainingTime {
    return fold(RemainingTime(userId, 0, 0)) { total, next -> total + next.calculateRemainingTime() }
}