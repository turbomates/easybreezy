package io.easybreezy.hr

import io.easybreezy.hr.infrastructure.CalendarRepository
import io.easybreezy.hr.infrastructure.LocationRepository
import io.easybreezy.hr.model.absence.Absences
import io.easybreezy.hr.model.absence.Reason
import io.easybreezy.hr.model.absence.WorkingHours
import io.easybreezy.hr.model.calendar.Calendars
import io.easybreezy.hr.model.calendar.Holidays
import io.easybreezy.hr.model.location.Locations
import io.easybreezy.hr.model.location.UserLocations
import io.easybreezy.infrastructure.exposed.toUUID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.util.UUID

internal fun Database.createAbsence(
    userId: UUID,
    startedAt: LocalDate = LocalDate.now(),
    endedAt: LocalDate = LocalDate.now().plusDays(20)
): UUID {
    return transaction(this) {
        val id = Absences.insert {
            it[this.startedAt] = startedAt
            it[this.endedAt] = endedAt
            it[comment] = "Test Comment"
            it[reason] = Reason.VACATION
            it[this.userId] = userId
        } get Absences.id
        id.toUUID()
    }
}

internal fun Database.createWorkingHour(
    userId: UUID,
    day: LocalDate = LocalDate.now().plusDays(20),
    count: Int = 5
): UUID {
    return transaction(this) {
        val id = WorkingHours.insert {
            it[this.day] = day
            it[this.count] = count
            it[this.userId] = userId
        } get WorkingHours.id
        id.toUUID()
    }
}

internal fun Database.createLocation(): UUID {
    return transaction(this) {
        val id = Locations.insert {
            it[name] = "Best Location For a Job"
            it[vacationDays] = 25
        } get Locations.id
        id.toUUID()
    }
}

internal fun Database.createUserLocation(
    userId: UUID,
    locationId: UUID,
    extraVacations: Int = 0,
    startedAt: LocalDate = LocalDate.now(),
    endedAt: LocalDate = LocalDate.now().plusDays(20)
): UUID {
    return transaction(this) {
        val id = UserLocations.insert {
            it[this.startedAt] = startedAt
            it[this.endedAt] = endedAt
            it[location] = LocationRepository().getOne(locationId).id
            it[this.extraVacationDays] = extraVacations
            it[this.userId] = userId
        } get UserLocations.id
        id.toUUID()
    }
}

internal fun Database.createCalendar(locationId: UUID): UUID {
    return transaction(this) {
        val id = Calendars.insert {
            it[name] = "Belarus"
            it[location] = LocationRepository().getOne(locationId).id
        } get Calendars.id

        id.toUUID()
    }
}

internal fun Database.createHoliday(calendarId: UUID): LocalDate {
    return transaction(this) {
        val day = Holidays.insert {
            it[name] = "New year"
            it[day] = LocalDate.now().plusDays(20)
            it[isWorkingDay] = false
            it[calendar] = CalendarRepository().getOne(calendarId).id
        } get Holidays.day

        day
    }
}
