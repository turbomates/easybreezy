package io.easybreezy.hr.application.event.command

import io.easybreezy.hr.model.event.WeekDays as WeekDaysEnum
import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.infrastructure.ktor.validate
import org.valiktor.Constraint
import org.valiktor.Validator
import org.valiktor.functions.isGreaterThan
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import java.time.LocalDateTime

class Validation {

    fun onCreateEvent(command: Open): List<Error> {
        return validate(command) {
            validate(Open::name).isNotNull().isNotBlank()
            validate(Open::owner).isNotNull()
            validate(Open::description).isNotBlank()
            validate(Open::startedAt).isNotNull().isGreaterThanOrEqualTo(LocalDateTime.now())
            validate(Open::endedAt).isGreaterThan(command.startedAt)
            validate(Open::days).isWeekDays()
        }
    }

    fun onUpdateEvent(command: UpdateDetails): List<Error> {
        return validate(command) {
            validate(UpdateDetails::id).isNotNull()
            validate(UpdateDetails::name).isNotBlank()
            validate(UpdateDetails::description).isNotBlank()
            validate(UpdateDetails::startedAt).isGreaterThanOrEqualTo(LocalDateTime.now())
            if (command.startedAt != null) {
                validate(UpdateDetails::endedAt).isGreaterThan(command.startedAt)
            }
            validate(UpdateDetails::days).isWeekDays()
        }
    }

    fun onChangeConditions(command: ChangeConditions): List<Error> {
        return validate(command) {
            validate(ChangeConditions::event).isNotNull()
        }
    }

    fun onAddParticipants(command: AddParticipants): List<Error> {
        return validate(command) {
            validate(AddParticipants::event).isNotNull()
            validate(AddParticipants::participants).isNotNull()
        }
    }

    fun onEnterEvent(command: Enter): List<Error> {
        return validate(command) {
            validate(Enter::event).isNotNull()
            validate(Enter::employeeId).isNotNull()
        }
    }

    fun onChangeVisitStatus(command: ChangeVisitStatus): List<Error> {
        return validate(command) {
            validate(ChangeVisitStatus::event).isNotNull()
            validate(ChangeVisitStatus::employeeId).isNotNull()
            validate(ChangeVisitStatus::status).isNotNull().isVisitStatus()
        }
    }
}

private object VisitStatus : Constraint {
    override val name: String
        get() = "Invalid visit status"
}

private fun <E> Validator<E>.Property<String?>.isVisitStatus() {
    this.validate(VisitStatus) { value ->
        io.easybreezy.hr.model.event.VisitStatus.values().any { it.name == value }
    }
}

private object WeekDays : Constraint {
    override val name: String
        get() = "Invalid week day"
}

fun <E> Validator<E>.Property<Iterable<String>?>.isWeekDays() {
    this.validate(WeekDays) { value ->
        value == null || value.all { day ->
            WeekDaysEnum.values().any { it.name == day }
        }
    }
}
