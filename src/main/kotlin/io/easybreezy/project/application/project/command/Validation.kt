package io.easybreezy.project.application.project.command

import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.infrastructure.ktor.validate
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotEmpty

class Validation {
    fun validate(command: New): List<Error> {

        return validate(command) {
            validate(New::name).hasSize(2, 255)
            validate(New::description).isNotBlank()
        }
    }

    fun validate(command: NewRole): List<Error> {

        return validate(command) {
            validate(NewRole::name).hasSize(2, 25)
            validate(NewRole::permissions).isNotEmpty()
        }
    }

    fun validate(command: NewTeam): List<Error> {

        return validate(command) {
            validate(NewTeam::name).hasSize(2, 25)
        }
    }
}
