package io.easybreezy.hr.application.calendar.command

import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.infrastructure.ktor.validate
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull

class Validation {

    fun onImportCalendar(command: ImportCalendar): List<Error> {
        return validate(command) {
            validate(ImportCalendar::name).isNotNull().isNotBlank()
            validate(ImportCalendar::locationId).isNotNull()
            validate(ImportCalendar::encodedCalendar).isNotNull().isNotBlank()
        }
    }

    fun onEditCalendar(command: EditCalendar): List<Error> {
        return validate(command) {
            validate(EditCalendar::id).isNotNull()
            validate(EditCalendar::name).isNotNull().isNotBlank()
            validate(EditCalendar::locationId).isNotNull()
        }
    }

    fun onAddHoliday(command: AddHoliday): List<Error> {
        return validate(command) {
            validate(AddHoliday::calendarId).isNotNull()
            validate(AddHoliday::day).isNotNull()
            validate(AddHoliday::name).isNotNull().isNotBlank()
            validate(AddHoliday::isWorkingDay).isNotNull()
        }
    }

    fun onEditHoliday(command: EditHoliday): List<Error> {
        return validate(command) {
            validate(EditHoliday::calendarId).isNotNull()
            validate(EditHoliday::day).isNotNull()
            validate(EditHoliday::name).isNotNull().isNotBlank()
            validate(EditHoliday::isWorkingDay).isNotNull()
        }
    }

    fun onRemoveHoliday(command: RemoveHoliday): List<Error> {
        return validate(command) {
            validate(RemoveHoliday::calendarId).isNotNull()
            validate(RemoveHoliday::day).isNotNull()
        }
    }
}
