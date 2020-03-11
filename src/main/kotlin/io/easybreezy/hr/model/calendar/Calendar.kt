package io.easybreezy.hr.model.calendar

import io.easybreezy.hr.model.location.Location
import io.easybreezy.hr.model.location.Locations
import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import java.time.LocalDate
import java.util.UUID

class Calendar private constructor(id: EntityID<UUID>) : AggregateRoot<UUID>(id) {
    private var name by Calendars.name
    private var location by Calendars.location
    private val holidays by Holiday.referrersOn(Holidays.calendar, true)

    fun addHoliday(day: LocalDate, holidayName: String, isWorkingDay: Boolean = false) {
        Holiday.create(this, day, holidayName, isWorkingDay)
    }

    fun editHoliday(day: LocalDate, holidayName: String, isWorkingDay: Boolean) {
        val holiday = holidays.first { it.day == day }
        holiday.edit(day, holidayName, isWorkingDay)
    }

    fun edit(name: String, location: Location) {
        this.name = name
        this.location = location.id
    }

    fun removeHoliday(day: LocalDate) {
        val holiday = holidays.first { it.day == day }
        holiday.delete()
    }

    companion object : PrivateEntityClass<UUID, Calendar>(object : Repository() {}) {
        fun create(name: String, location: Location): Calendar {
            return Calendar.new {
                this.name = name
                this.location = location.id
            }
        }
    }

    abstract class Repository : EntityClass<UUID, Calendar>(Calendars, Calendar::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Calendar {
            return Calendar(entityId)
        }
    }
}

object Calendars : UUIDTable("calendars") {
    val name = text("name").uniqueIndex()
    val location = reference("location", Locations).uniqueIndex()
}