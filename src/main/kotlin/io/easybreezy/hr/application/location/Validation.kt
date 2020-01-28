package io.easybreezy.hr.application.location

import com.google.inject.Inject
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.validate

class Validation @Inject constructor() {

    fun onCreateLocation(command: CreateLocation) {
        validate(command) {
            validate(CreateLocation::name).isNotNull().isNotBlank()
        }
    }
}