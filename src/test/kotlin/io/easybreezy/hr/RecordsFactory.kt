package io.easybreezy.hr

import io.easybreezy.createMember
import io.easybreezy.hr.infrastructure.CalendarRepository
import io.easybreezy.hr.infrastructure.LocationRepository
import io.easybreezy.hr.model.absence.Absences
import io.easybreezy.hr.model.absence.Reason
import io.easybreezy.hr.model.absence.WorkingHours
import io.easybreezy.hr.model.calendar.Calendars
import io.easybreezy.hr.model.calendar.Holidays
import io.easybreezy.hr.model.event.ConditionsTable
import io.easybreezy.hr.model.event.Events
import io.easybreezy.hr.model.event.Participants
import io.easybreezy.hr.model.event.VisitStatus
import io.easybreezy.hr.model.location.Location.Companion.MIN_VACATIONS_DAYS
import io.easybreezy.hr.model.location.Locations
import io.easybreezy.hr.model.location.UserLocations
import io.easybreezy.user.model.Users
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

internal fun Database.createAbsence(
    userId: UUID = UUID.randomUUID(),
    startedAt: LocalDate = LocalDate.now(),
    endedAt: LocalDate = LocalDate.now().plusDays(20),
    isApproved: Boolean = true
): UUID {
    return transaction(this) {
        val id = Absences.insert {
            it[this.startedAt] = startedAt
            it[this.endedAt] = endedAt
            it[comment] = "Test Comment"
            it[reason] = Reason.VACATION
            it[this.userId] = userId
            it[this.isApproved] = isApproved
        } get Absences.id
        id.value
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
        id.value
    }
}

internal fun Database.createLocation(): UUID {
    return transaction(this) {
        val id = Locations.insert {
            it[name] = "Best Location For a Job"
            it[vacationDays] = MIN_VACATIONS_DAYS + 1
        } get Locations.id
        id.value
    }
}

internal fun Database.createUserLocation(
    userId: UUID,
    locationId: UUID,
    extraVacations: Int = 0,
    startedAt: LocalDate = LocalDate.now().minusDays(5),
    endedAt: LocalDate? = LocalDate.now().plusDays(20)
): UUID {
    return transaction(this) {
        val id = UserLocations.insert {
            it[this.startedAt] = startedAt
            it[this.endedAt] = endedAt
            it[location] = LocationRepository().getOne(locationId).id
            it[this.extraVacationDays] = extraVacations
            it[this.userId] = EntityID(userId, Users)
        } get UserLocations.id
        id.value
    }
}

internal fun Database.createCalendar(locationId: UUID): UUID {
    return transaction(this) {
        val id = Calendars.insert {
            it[name] = "Belarus"
            it[location] = LocationRepository().getOne(locationId).id
        } get Calendars.id

        id.value
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

internal fun Database.createEvent(isPrivate: Boolean = false): UUID {
    return transaction(this) {
        val memberId = createMember()
        val event = Events.insert {
            it[name] = "Test event"
            it[startedAt] = LocalDateTime.now().minusDays(5)
            it[conditions[ConditionsTable.isPrivate]] = isPrivate
            it[conditions[ConditionsTable.isFreeEntry]] = true
            it[conditions[ConditionsTable.isRepeatable]] = false
            it[owner] = memberId
        } get Events.id
        event.value
    }
}

internal fun Database.createParticipant(eventId: UUID, employee: UUID): UUID {
    return transaction(this) {
        val participant = Participants.insert {
            it[event] = EntityID(eventId, Events)
            it[this.employee] = employee
            it[visitStatus] = VisitStatus.WAIT_RESPONSE
        } get Participants.id
        participant.value
    }
}
