package io.easybreezy.hr

import io.easybreezy.hr.infrastructure.LocationRepository
import io.easybreezy.hr.model.absence.Absences
import io.easybreezy.hr.model.absence.Reason
import io.easybreezy.hr.model.absence.WorkingHours
import io.easybreezy.hr.model.location.Locations
import io.easybreezy.hr.model.location.UserLocations
import io.easybreezy.infrastructure.exposed.toUUID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.util.UUID

internal fun Database.createAbsence(userId: UUID): UUID {
    return transaction(this) {
        val id = Absences.insert {
            it[startedAt] = LocalDate.now()
            it[endedAt] = LocalDate.now().plusDays(20)
            it[comment] = "Test Comment"
            it[reason] = Reason.VACATION
            it[this.userId] = userId
        } get Absences.id
        id.toUUID()
    }
}

internal fun Database.createWorkingHour(userId: UUID): UUID {
    return transaction(this) {
        val id = WorkingHours.insert {
            it[day] = LocalDate.now().plusDays(20)
            it[count] = 5
            it[this.userId] = userId
        } get WorkingHours.id
        id.toUUID()
    }
}

internal fun Database.createLocation(): UUID {
    return transaction(this) {
        val id = Locations.insert {
            it[name] = "Best Location For a Job"
        } get Locations.id
        id.toUUID()
    }
}

internal fun Database.createUserLocation(userId: UUID, locationId: UUID): UUID {
    return transaction(this) {
        val id = UserLocations.insert {
            it[startedAt] = LocalDate.now()
            it[endedAt] = LocalDate.now().plusDays(20)
            it[location] = LocationRepository().getOne(locationId).id
            it[this.userId] = userId
        } get UserLocations.id
        id.toUUID()
    }
}
