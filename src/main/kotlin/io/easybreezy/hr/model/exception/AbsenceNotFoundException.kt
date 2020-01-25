package io.easybreezy.hr.model.exception

import java.util.UUID

class AbsenceNotFoundException(id: UUID): Exception("Absence with id $id not found")