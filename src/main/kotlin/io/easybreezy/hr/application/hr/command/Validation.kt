package io.easybreezy.hr.application.hr.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.infrastructure.ktor.validate
import org.valiktor.functions.*

class Validation @Inject constructor() {

    fun onHire(command: Hire): List<Error> {

        return validate(command) {
            validate(Hire::position).hasSize(2, 100)
            validate(Hire::salary).isPositiveOrZero()
        }
    }

    fun onFire(command: Fire): List<Error> {

        return validate(command) {
            validate(Fire::comment).isNotBlank()
        }
    }

    fun onWriteNote(command: WriteNote): List<Error> {
        return validate(command) {
            validate(WriteNote::text).isNotBlank()
        }
    }

    fun onCorrectNote(command: CorrectNote): List<Error> {
        return validate(command) {
            validate(CorrectNote::text).isNotBlank()
        }
    }

    fun onApplyPosition(command: ApplyPosition): List<Error> {
        return validate(command) {
            validate(ApplyPosition::position).hasSize(2, 100)
        }
    }

    fun onApplySalary(command: ApplySalary): List<Error> {
        return validate(command) {
            validate(ApplySalary::amount).isPositiveOrZero()
            validate(ApplySalary::comment).isNotBlank()
        }
    }

    fun onCorrectSalary(command: CorrectSalary): List<Error> {
        return validate(command) {
            validate(CorrectSalary::correctedAmount).isPositiveOrZero()
        }
    }

    fun onSpecifySkills(command: SpecifySkills): List<Error> {
        return validate(command) {
            validate(SpecifySkills::skills).isNotEmpty()
        }
    }

    fun onUpdateBio(command: UpdateBio): List<Error> {
        return validate(command) {
            validate(UpdateBio::bio).isNotEmpty()
        }
    }

    fun onUpdateBirthday(command: UpdateBirthday): List<Error> {
        return validate(command) {
            validate(UpdateBirthday::birthday).isNotNull()
        }
    }
}
