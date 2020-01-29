package io.easybreezy.hr.model.location

import java.util.UUID

class LocationNotFoundException(id: UUID) : Exception("Location with id $id not found")
class UserLocationNotFoundException(id: UUID) : Exception("User Location with id $id not found")