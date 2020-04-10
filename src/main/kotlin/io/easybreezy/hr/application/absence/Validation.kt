package io.easybreezy.hr.application.absence

import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.infrastructure.ktor.validate
import org.valiktor.functions.isLessThan
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.functions.isPositive

class Validation {

    companion object {
        private const val FULL_WORKING_HOURS = 8
    }

    fun onCreateAbsence(command: CreateAbsence): List<Error> {
        return validate(command) {
            validate(CreateAbsence::startedAt).isNotNull().isLessThan(command.endedAt)
            validate(CreateAbsence::endedAt).isNotNull()
            validate(CreateAbsence::comment).isNotBlank()
            validate(CreateAbsence::reason).isNotNull().isNotBlank()
            validate(CreateAbsence::userId).isNotNull()
        }
    }

    fun onUpdateAbsence(command: UpdateAbsence): List<Error> {
        return validate(command) {
            validate(UpdateAbsence::startedAt).isNotNull().isLessThan(command.endedAt)
            validate(UpdateAbsence::endedAt).isNotNull()
            validate(UpdateAbsence::comment).isNotBlank()
            validate(UpdateAbsence::reason).isNotNull().isNotBlank()
            validate(UpdateAbsence::id).isNotNull()
        }
    }

    fun onNoteWorkingHours(command: NoteWorkingHours): List<Error> {
        val commandErrors: List<Error> = validate(command) {
            validate(NoteWorkingHours::userId).isNotNull()
        }
        return command.workingHours.fold(commandErrors) { acc, value ->
            return acc + validate(value) {
                validate(WorkingHour::day).isNotNull()
                validate(WorkingHour::count).isNotNull().isPositive().isLessThan(FULL_WORKING_HOURS)
            }
        }
    }

    fun onEditWorkingHours(command: EditWorkingHours): List<Error> {
        val errors = emptyList<Error>()
        return command.workingHours.toList().fold(errors) { acc, pair ->
            return acc + validate(pair.second) {
                validate(WorkingHour::day).isNotNull()
                validate(WorkingHour::count).isNotNull().isPositive().isLessThan(FULL_WORKING_HOURS)
            }
        }
    }
}
