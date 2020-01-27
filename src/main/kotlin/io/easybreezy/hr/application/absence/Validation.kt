package io.easybreezy.hr.application.absence

import com.google.inject.Inject
import io.easybreezy.user.infrastructure.UserRepository
import org.valiktor.Constraint
import org.valiktor.Validator
import org.valiktor.functions.isLessThan
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.functions.isPositive
import org.valiktor.validate
import java.util.UUID

class Validation @Inject constructor(private val repository: UserRepository) {

    companion object {
        private const val FULL_WORKING_HOURS = 8
    }

    object UserExists : Constraint {
        override val name: String
            get() = "User with this id not found"
    }

    private fun <E> Validator<E>.Property<UUID?>.isUserExists(): Validator<E>.Property<UUID?> =
        this.validate(UserExists) { value ->
            repository.find(value!!) != null
        }

    fun onCreateAbsence(command: CreateAbsence) {
        validate(command) {
            validate(CreateAbsence::startedAt).isNotNull().isLessThan(command.endedAt)
            validate(CreateAbsence::endedAt).isNotNull()
            validate(CreateAbsence::comment).isNotBlank()
            validate(CreateAbsence::reason).isNotNull().isNotBlank()
            validate(CreateAbsence::userId).isNotNull().isUserExists()
        }
    }

    fun onUpdateAbsence(command: UpdateAbsence) {
        validate(command) {
            validate(UpdateAbsence::startedAt).isNotNull().isLessThan(command.endedAt)
            validate(UpdateAbsence::endedAt).isNotNull()
            validate(UpdateAbsence::comment).isNotBlank()
            validate(UpdateAbsence::reason).isNotNull().isNotBlank()
            validate(UpdateAbsence::id).isNotNull()
        }
    }

    fun onNoteWorkingHours(command: NoteWorkingHours) {
        validate(command) {
            validate(NoteWorkingHours::userId).isNotNull().isUserExists()
        }
        command.workingHours.forEach {
            validate(it) {
                validate(WorkingHour::day).isNotNull()
                validate(WorkingHour::count).isNotNull().isPositive().isLessThan(FULL_WORKING_HOURS)
            }
        }
    }

    fun onEditWorkingHours(command: EditWorkingHours) {
        command.workingHours.forEach {
            validate(it.value) {
                validate(WorkingHour::day).isNotNull()
                validate(WorkingHour::count).isNotNull().isPositive().isLessThan(FULL_WORKING_HOURS)
            }
        }
    }

    fun onRemoveWorkingHours(command: RemoveWorkingHours) {
        validate(command) {
            validate(RemoveWorkingHours::workingHours).isNotNull().isNotEmpty()
        }
    }
}
