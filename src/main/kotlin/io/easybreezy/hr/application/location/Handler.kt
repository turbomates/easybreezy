package io.easybreezy.hr.application.location

import io.easybreezy.hr.model.location.Location

class Handler {

    fun handleCreateLocation(command: CreateLocation) {
        Location.create(command.name)
    }
}