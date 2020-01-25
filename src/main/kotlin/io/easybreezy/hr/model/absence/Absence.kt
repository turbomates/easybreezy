package io.easybreezy.hr.model.absence

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.infrastructure.postgresql.PGEnum
import io.easybreezy.user.model.User
import io.easybreezy.user.model.Users
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.date
import java.time.LocalDate
import java.util.UUID

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
    val userId = reference("user_id", Users)
}

class Absence private constructor(id: EntityID<UUID>) : UUIDEntity(id) {
    private var startedAt by Absences.startedAt
    private var endedAt by Absences.endedAt
    private var comment by Absences.comment
    private var reason by Absences.reason
    private var userId by Absences.userId

    companion object : PrivateEntityClass<UUID, Absence>(object : Absence.Repository() {}) {
        fun create(startedAt: LocalDate, endedAt: LocalDate, reason: Reason, user: User, comment: String? = null): Absence {
            return Absence.new {
                this.startedAt = startedAt
                this.endedAt = endedAt
                this.reason = reason
                this.userId = user.id
                this.comment = comment
            }
        }
    }

    fun edit(startedAt: LocalDate, endedAt: LocalDate, reason: Reason, comment: String? = null) {
        this.startedAt = startedAt
        this.endedAt = endedAt
        this.reason = reason
        this.comment = comment
    }

    abstract class Repository : UUIDEntityClass<Absence>(Absences, Absence::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Absence {
            return Absence(entityId)
        }
    }
}

enum class Reason {
    VACATION, SICK, DAYON, PERSONAL
}
