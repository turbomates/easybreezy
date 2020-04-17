package io.easybreezy.hr.application.location

import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.infrastructure.ktor.validate
import org.valiktor.functions.*

class Validation {
    fun onCreateLocation(command: CreateLocation): List<Error> {
        return validate(command) {
            validate(CreateLocation::name).isNotNull().isNotBlank()
            validate(CreateLocation::vacationDays).isNotNull().isGreaterThanOrEqualTo(24)
        }
    }

    fun onAssignLocation(command: AssignLocation): List<Error> {
        return validate(command) {
            validate(AssignLocation::userId).isNotNull()
            validate(AssignLocation::locationId).isNotNull()
        }
    }

    fun onEditUserLocation(command: EditUserLocation): List<Error> {
        return validate(command) {
            validate(EditUserLocation::startedAt).isNotNull()
            validate(EditUserLocation::locationId).isNotNull()
        }
    }
}
