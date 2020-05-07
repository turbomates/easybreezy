package io.easybreezy.hr.model.absence

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.infrastructure.postgresql.PGEnum
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.date
import java.time.LocalDate
import java.util.UUID

class Absence private constructor(id: EntityID<UUID>) : UUIDEntity(id) {
    private var startedAt by Absences.startedAt
    private var endedAt by Absences.endedAt
    private var reason by Absences.reason
    private var userId by Absences.userId
    private var isApproved by Absences.isApproved
    var comment by Absences.comment

    companion object : PrivateEntityClass<UUID, Absence>(object : Repository() {}) {
        fun create(startedAt: LocalDate, endedAt: LocalDate, reason: Reason, userId: UUID): Absence {
            return Absence.new {
                this.startedAt = startedAt
                this.endedAt = endedAt
                this.reason = reason
                this.userId = userId
                this.isApproved = false
            }
        }
    }

    fun edit(startedAt: LocalDate, endedAt: LocalDate, reason: Reason) {
        require(!isApproved) { "Approved absence can not be edited" }
        this.startedAt = startedAt
        this.endedAt = endedAt
        this.reason = reason
    }

    fun approve() {
        isApproved = true
    }

    abstract class Repository : UUIDEntityClass<Absence>(Absences, Absence::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Absence {
            return Absence(entityId)
        }
    }
}

object Absences : UUIDTable("absences") {
    val startedAt = date("started_at").uniqueIndex()
    val endedAt = date("ended_at").uniqueIndex()
    val comment = text("comment").nullable()
    val reason = customEnumeration(
        "reason",
        "reason",
        { Reason.valueOf(it as String) },
        { PGEnum("absence_reason", it) }
    )
    val userId = uuid("user_id")
    val isApproved = bool("is_approved").default(false)
}

enum class Reason {
    VACATION, SICK, DAYON, PERSONAL
}
