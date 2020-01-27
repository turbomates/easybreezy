package io.easybreezy.hr.model.exception

import java.util.UUID

class WorkingHourNotFoundException(id: UUID) : Exception("Working hour with id $id not found")
