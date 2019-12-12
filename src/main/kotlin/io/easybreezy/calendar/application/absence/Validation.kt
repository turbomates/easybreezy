package io.easybreezy.calendar.application.absence

import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.validate

class Validation {
    fun onCreate(command: Create) {
        validate(command) {
            validate(Create::startedAt).isNotNull()
            validate(Create::endedAt).isNotNull()
            validate(Create::comment).isNotBlank()
            validate(Create::reason).isNotNull().isNotBlank()
            validate(Create::userId).isNotNull()
        }
    }

    fun onUpdate(command: Update) {
        TODO("Not implemented")
    }
}