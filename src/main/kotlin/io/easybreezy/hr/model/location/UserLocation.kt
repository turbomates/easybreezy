package io.easybreezy.hr.model.location

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.infrastructure.exposed.dao.embedded
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
            endedAt: LocalDate,
            location: Location,
            userId: UUID
        ): UserLocation {
            return UserLocation.new {
                this.startedAt = startedAt
                this.endedAt = endedAt
                this.location = location
                this.userId = userId
            }
        }
    }

    fun edit(startedAt: LocalDate, endedAt: LocalDate, location: Location) {
        this.startedAt = startedAt
        this.endedAt = endedAt
        this.location = location
    }

    fun addVacationDays(days: Int) {
        this.extraVacationDays = days
    }

    abstract class Repository : UUIDEntityClass<UserLocation>(UserLocations, UserLocation::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): UserLocation {
            return UserLocation(entityId)
        }
    }
}

object UserLocations : UUIDTable("user_locations") {
    val startedAt = date("started_at").uniqueIndex()
    val endedAt = date("ended_at").uniqueIndex()
    val location = reference("location", Locations)
    val userId = uuid("user_id")
    val extraVacationDays = integer("extra_vacation_days").nullable()
}
