package io.easybreezy.hr.application.calendar.command

import com.google.inject.Inject
import io.easybreezy.hr.infrastructure.CalendarRepository
import io.easybreezy.hr.infrastructure.LocationRepository
import io.easybreezy.hr.model.calendar.Calendar
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.infrastructure.exposed.toLocalDate
import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.property.DtStart
import net.fortuna.ical4j.model.property.Summary
import java.io.StringReader
import java.util.Base64
import java.util.UUID

class Handler @Inject constructor(
    private val locationRepository: LocationRepository,
    private val calendarRepository: CalendarRepository,
    private val transaction: TransactionManager
) {

    suspend fun importCalendar(command: ImportCalendar) {
        transaction {
            val location = locationRepository.getOne(command.locationId)

            val decodedCalendar = String(Base64.getDecoder().decode(command.encodedCalendar))
            val reader = StringReader(decodedCalendar)
            val iCalendar = CalendarBuilder().build(reader)

            val calendar = Calendar.create(command.name, location)

            for (component in iCalendar.components) {
                val day = component.getProperty<DtStart>("DTSTART").date.toLocalDate()
                val summary = component.getProperty<Summary>("SUMMARY").value
                val holidayName = summary.replace(Regex("(\\w*:)"), "")

                calendar.addHoliday(day, holidayName)
            }
        }
    }

    suspend fun editCalendar(command: EditCalendar) {
        transaction {
            val calendar = calendarRepository.getOne(command.id)
            calendar.edit(command.name, locationRepository.getOne(command.locationId))
        }
    }

    suspend fun addHoliday(command: AddHoliday) {
        transaction {
            val calendar = calendarRepository.getOne(command.calendarId)
            calendar.addHoliday(command.day, command.name, command.isWorkingDay)
        }
    }

    suspend fun editHoliday(command: EditHoliday) {
        transaction {
            val calendar = calendarRepository.getOne(command.calendarId)
            calendar.editHoliday(command.day, command.name, command.isWorkingDay)
        }
    }

    suspend fun removeHoliday(command: RemoveHoliday) {
        transaction {
            val calendar = calendarRepository.getOne(command.calendarId)
            calendar.removeHoliday(command.day)
        }
    }

    suspend fun removeCalendar(id: UUID) {
        transaction {
            calendarRepository.remove(id)
        }
    }
}
