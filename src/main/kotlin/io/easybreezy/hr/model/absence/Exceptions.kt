package io.easybreezy.hr.model.absence

import java.util.UUID

class AbsenceNotFoundException(id: UUID) : Exception("Absence with id $id not found")
class WorkingHourNotFoundException(id: UUID) : Exception("Working hour with id $id not found")
class CalendarNotFoundException(id: UUID) : Exception("Calendar with id $id not found")
