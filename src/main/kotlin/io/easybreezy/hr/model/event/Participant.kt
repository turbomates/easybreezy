package io.easybreezy.hr.model.event

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.infrastructure.postgresql.PGEnum
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

typealias EmployeeId = UUID

class Participant private constructor(id: EntityID<UUID>) : UUIDEntity(id) {
    private var event by Event referencedOn Participants.event
    var employee: EmployeeId by Participants.employee
    private var visitStatus by Participants.visitStatus

    companion object : PrivateEntityClass<UUID, Participant>(object : Participant.Repository() {}) {
        fun create(event: Event, employee: UUID): Participant {
            return Participant.new {
                this.event = event
                this.employee = employee
                this.visitStatus = VisitStatus.WAIT_RESPONSE
            }
        }
    }

    abstract class Repository : EntityClass<UUID, Participant>(Participants, Participant::class.java) {
        override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Participant {
            return Participant(entityId)
        }
    }

    fun changeVisitStatus(status: VisitStatus) {
        require(status != VisitStatus.WAIT_RESPONSE) { "Trying to change visit status to initial" }
        visitStatus = status
    }
}

enum class VisitStatus {
    WAIT_RESPONSE, WILL_COME, MAYBE_COME, NOT_COME
}

object Participants : UUIDTable("event_participants") {
    val event = reference("event", Events)
    val employee = uuid("employee")
    val visitStatus = customEnumeration(
        "visit_status",
        "event_participant_visit_status",
        { value -> VisitStatus.valueOf(value as String) },
        { PGEnum("event_participant_visit_status", it) }).default(VisitStatus.WAIT_RESPONSE)

    init {
        uniqueIndex(event, employee)
    }
}
