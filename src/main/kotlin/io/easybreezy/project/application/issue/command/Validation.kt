package io.easybreezy.project.application.issue.command

import io.easybreezy.infrastructure.ktor.validate
import org.valiktor.functions.isNotBlank
import io.easybreezy.infrastructure.ktor.Error

class Validation  {

    fun validateCommand(command: New): List<Error> {

        return validate(command) {
            validate(New::content).isNotBlank()
        }
    }

    fun validateCommand(command: Update): List<Error> {

        return validate(command) {
            validate(Update::text).isNotBlank()
        }
    }
}