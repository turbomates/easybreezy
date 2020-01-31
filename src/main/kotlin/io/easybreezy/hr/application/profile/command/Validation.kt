package io.easybreezy.hr.application.profile.command

import io.easybreezy.hr.model.profile.Profiles
import org.valiktor.Constraint
import org.valiktor.Validator
import org.valiktor.validate

class Validation {

    object GenderConstraint : Constraint {
        override val name: String
            get() = "Non valid gender value"
    }

    private fun <E> Validator<E>.Property<String?>.isValidGender(): Validator<E>.Property<String?> =
        this.validate(GenderConstraint) { value ->
            Profiles.Gender.values().any { it.name == value }
        }

    fun onUpdatePersonalData(command: UpdatePersonalData) {
        validate(command) {
            validate(UpdatePersonalData::gender).isValidGender()
        }
    }
}