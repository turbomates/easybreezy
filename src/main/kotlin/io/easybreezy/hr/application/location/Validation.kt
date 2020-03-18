package io.easybreezy.hr.application.location

import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.infrastructure.ktor.validate
import org.valiktor.functions.isLessThan
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull

class Validation {
    fun onCreateLocation(command: CreateLocation): List<Error> {
        return validate(command) {
            validate(CreateLocation::name).isNotNull().isNotBlank()
        }
    }

    fun onAssignLocation(command: AssignLocation): List<Error> {
        return validate(command) {
            validate(AssignLocation::startedAt).isNotNull().isLessThan(command.endedAt)
            validate(AssignLocation::endedAt).isNotNull()
            validate(AssignLocation::userId).isNotNull()
            validate(AssignLocation::locationId).isNotNull()
        }
    }

    fun onEditUserLocation(command: EditUserLocation): List<Error> {
        return validate(command) {
            validate(EditUserLocation::startedAt).isNotNull().isLessThan(command.endedAt)
            validate(EditUserLocation::endedAt).isNotNull()
            validate(EditUserLocation::locationId).isNotNull()
        }
    }
}
