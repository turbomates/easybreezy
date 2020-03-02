package io.easybreezy.hr.application.profile.command

import io.easybreezy.hr.model.profile.PersonalDataTable
import io.easybreezy.hr.model.profile.Profiles
import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.infrastructure.ktor.validate
import org.valiktor.Constraint
import org.valiktor.Validator

class Validation {

    object GenderConstraint : Constraint {
        override val name: String
            get() = "Non valid gender value"
    }

    private fun <E> Validator<E>.Property<String?>.isValidGender(): Validator<E>.Property<String?> =
        this.validate(GenderConstraint) { value ->
            PersonalDataTable.Gender.values().any { it.name == value }
        }

    fun onUpdatePersonalData(command: UpdatePersonalData): List<Error> {
        return validate(command) {
            validate(UpdatePersonalData::gender).isValidGender()
        }
    }
}
