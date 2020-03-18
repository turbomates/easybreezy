package io.easybreezy.hr.model.calendar

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.date
import java.time.LocalDate
import java.util.UUID

class Holiday private constructor(id: EntityID<UUID>) : UUIDEntity(id) {
    var day by Holidays.day
    private var name by Holidays.name
    /**
     * True means weekend rescheduled to working day
     */
    private var isWorkingDay by Holidays.isWorkingDay
    var calendar by Calendar referencedOn Holidays.calendar
        private set

    fun edit(day: LocalDate, name: String, isWorkingDay: Boolean) {
        this.day = day
        this.name = name
        this.isWorkingDay = isWorkingDay
    }

    companion object : PrivateEntityClass<UUID, Holiday>(object : Repository() {}) {
        fun create(calendar: Calendar, day: LocalDate, name: String, isWorkingDay: Boolean) = Holiday.new {
            this.calendar = calendar
            this.day = day
            this.name = name
            this.isWorkingDay = isWorkingDay
        }
    }

    abstract class Repository : UUIDEntityClass<Holiday>(Holidays, Holiday::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Holiday {
            return Holiday(entityId)
        }
    }
}

object Holidays : UUIDTable("holidays") {
    val day = date("day")
    val name = text("name")
    val isWorkingDay = bool("is_working_day")
    val calendar = reference("calendar", Calendars, onDelete = ReferenceOption.CASCADE)

    init {
        uniqueIndex(day, calendar)
    }
}
