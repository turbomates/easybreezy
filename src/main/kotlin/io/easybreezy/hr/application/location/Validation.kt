package io.easybreezy.hr.application.location

import com.google.inject.Inject
import org.valiktor.functions.isLessThan
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.validate

class Validation @Inject constructor() {

    fun onCreateLocation(command: CreateLocation) {
        validate(command) {
            validate(CreateLocation::name).isNotNull().isNotBlank()
        }
    }

    fun onAssignLocation(command: AssignLocation) {
        validate(command) {
            validate(AssignLocation::startedAt).isNotNull().isLessThan(command.endedAt)
            validate(AssignLocation::endedAt).isNotNull()
            validate(AssignLocation::userId).isNotNull()
            validate(AssignLocation::locationId).isNotNull()
        }
    }

    fun onEditUserLocation(command: EditUserLocation) {
        validate(command) {
            validate(EditUserLocation::startedAt).isNotNull().isLessThan(command.endedAt)
            validate(EditUserLocation::endedAt).isNotNull()
            validate(EditUserLocation::locationId).isNotNull()
        }
    }
}