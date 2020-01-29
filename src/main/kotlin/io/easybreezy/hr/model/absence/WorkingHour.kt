package io.easybreezy.hr.model.absence

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.date
import java.time.LocalDate
import java.util.UUID

/**
 * Contains how much employee worked in certain day when he didn't work full day.
 */
class WorkingHour private constructor(id: EntityID<UUID>) : UUIDEntity(id) {
    private var day by WorkingHours.day
    private var count by WorkingHours.count
    private var userId by WorkingHours.userId

    companion object : PrivateEntityClass<UUID, WorkingHour>(object : Repository() {}) {
        fun create(day: LocalDate, count: Int, userId: UUID): WorkingHour {
            return WorkingHour.new {
                this.day = day
                this.count = count
                this.userId = userId
            }
        }
    }

    abstract class Repository : UUIDEntityClass<WorkingHour>(WorkingHours, WorkingHour::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): WorkingHour {
            return WorkingHour(entityId)
        }
    }

    fun edit(day: LocalDate, count: Int) {
        this.day = day
        this.count = count
    }
}

object WorkingHours : UUIDTable("working_hours") {
    val day = date("day").uniqueIndex()
    val count = integer("count")
    val userId = uuid("user_id")
}
