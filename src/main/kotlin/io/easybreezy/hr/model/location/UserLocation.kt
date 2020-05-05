package io.easybreezy.hr.model.location

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.user.model.Users
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.date
import java.time.LocalDate
import java.util.UUID

class UserLocation private constructor(id: EntityID<UUID>) : UUIDEntity(id) {
    private var startedAt by UserLocations.startedAt
    private var endedAt by UserLocations.endedAt
    private var location by Location referencedOn UserLocations.location
    private var userId by UserLocations.userId
    /** If employee already have had vacation days before starting location*/
    private var extraVacationDays by UserLocations.extraVacationDays

    companion object : PrivateEntityClass<UUID, UserLocation>(object : Repository() {}) {
        fun create(
            startedAt: LocalDate,
            location: Location,
            userId: UUID
        ): UserLocation {
            if (startedAt > LocalDate.now().plusDays(1)) throw InvalidStart(startedAt)
            return UserLocation.new {
                this.startedAt = startedAt
                this.location = location
                this.userId = EntityID(userId, Users)
            }
        }
    }

    fun edit(startedAt: LocalDate, location: Location) {
        if (startedAt > LocalDate.now().plusDays(1)) throw InvalidStart(startedAt)
        this.startedAt = startedAt
        this.location = location
    }

    fun addVacationDays(days: Int) {
        this.extraVacationDays = days
    }

    fun close() {
        if (endedAt != null) throw Exception("User location have been already closed")
        endedAt = LocalDate.now()
    }

    abstract class Repository : UUIDEntityClass<UserLocation>(UserLocations, UserLocation::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): UserLocation {
            return UserLocation(entityId)
        }
    }
}

object UserLocations : UUIDTable("user_locations") {
    val startedAt = date("started_at").uniqueIndex()
    val endedAt = date("ended_at").uniqueIndex().nullable()
    val location = reference("location", Locations)
    val userId = reference("user_id", Users)
    val extraVacationDays = integer("extra_vacation_days").nullable()
}
