package io.easybreezy.hr.model.event

import io.easybreezy.hr.model.location.Location
import io.easybreezy.hr.model.location.Locations
import io.easybreezy.infrastructure.event.event.BecamePublic
import io.easybreezy.infrastructure.event.event.Cancelled
import io.easybreezy.infrastructure.event.event.EntryOpened
import io.easybreezy.infrastructure.event.event.Opened
import io.easybreezy.infrastructure.event.event.ParticipantsAdded
import io.easybreezy.infrastructure.event.event.StartChanged
import io.easybreezy.infrastructure.exposed.dao.AggregateRoot
import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import io.easybreezy.infrastructure.exposed.dao.embedded
import io.easybreezy.infrastructure.exposed.type.jsonb
import io.easybreezy.infrastructure.postgresql.PGEnum
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.builtins.set
import org.jetbrains.exposed.dao.EntityChangeType
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityHook
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.dao.toEntity
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime
import java.util.UUID

typealias EventId = UUID
typealias Owner = UUID

class Event private constructor(id: EntityID<EventId>) : AggregateRoot<EventId>(id) {
    private var name by Events.name
    private var startedAt by Events.startedAt
    private var endedAt by Events.endedAt
    private var days by Events.days
    private val participants by Participant referrersOn Participants.event
    private var status by Events.status
    private var conditions by Events.conditions
    private var description by Events.description
    private var location by Location optionalReferencedOn Events.location
    private var createdAt by Events.createdAt
    private var updatedAt by Events.updatedAt
    private var owner by Events.owner

    fun changeName(name: String) {
        this.name = name
    }

    fun changeDescription(description: String) {
        this.description = description
    }

    fun assignLocation(location: Location) {
        this.location = location
    }

    fun cancel() {
        require(this.status == Status.OPENED) { "Event should be opened" }
        status = Status.CANCELLED
        addEvent(Cancelled(id.value))
    }

    fun makePrivate() {
        conditions.makePrivate()
    }

    fun makePublic() {
        conditions.makePublic()
        this.addEvent(BecamePublic(this.id.value))
    }

    fun closeEntry() {
        conditions.closeEntry()
    }

    fun openEntry() {
        conditions.openEntry()
        this.addEvent(EntryOpened(this.id.value))
    }

    fun makeRepeatable() {
        conditions.makeRepeatable()
    }

    fun makeDoneOnce() {
        conditions.makeDoneOnce()
    }

    fun addParticipants(participants: List<EmployeeId>) {
        participants.map {
            Participant.create(this, it)
        }
        addEvent(ParticipantsAdded(id.value, participants))
    }

    fun enter(employee: EmployeeId) {
        require(!conditions.isPrivate && conditions.isFreeEntry) { "Only the event owner can adds participants" }
        Participant.create(this, employee)
    }

    fun holdOnDays(days: Set<WeekDays>) {
        this.days = days.map { it.name }.toSet()
    }

    fun specifyStartTime(startedAt: LocalDateTime) {
        if (endedAt != null) require(startedAt < endedAt) { "Event can not start after end" }
        val oldStartedAt = this.startedAt
        this.startedAt = startedAt
        addEvent(StartChanged(id.value, oldStartedAt, startedAt))
    }

    fun specifyEndTime(endedAt: LocalDateTime) {
        require(endedAt > startedAt) { "Event can not end before start" }
        this.endedAt = endedAt
    }

    fun changeParticipantVisitStatus(employeeId: EmployeeId, status: VisitStatus) {
        val participant = participants.find { it.employee == employeeId }
        participant?.changeVisitStatus(status)
    }

    companion object : PrivateEntityClass<EventId, Event>(object : Repository() {}) {
        fun open(
            name: String,
            startedAt: LocalDateTime,
            owner: Owner,
            conditions: Conditions
        ): Event {
            return Event.new {
                this.name = name
                this.startedAt = startedAt
                this.owner = owner
                this.conditions = conditions
                this.status = Status.OPENED
                this.addEvent(
                    Opened(
                        this.id.value,
                        conditions.isPrivate,
                        conditions.isFreeEntry,
                        conditions.isRepeatable
                    )
                )
            }
        }
    }

    abstract class Repository : EntityClass<UUID, Event>(Events, Event::class.java) {
        init {
            EntityHook.subscribe { action ->
                if (action.changeType == EntityChangeType.Updated) {
                    action.toEntity(this)?.updatedAt = LocalDateTime.now()
                }
            }
        }

        override fun createInstance(entityId: EntityID<EventId>, row: ResultRow?): Event {
            return Event(entityId)
        }
    }
}

enum class Status {
    OPENED, CANCELLED
}

enum class WeekDays {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

object Events : UUIDTable("events") {
    val name = varchar("name", 255)
    val description = text("description").nullable()
    val startedAt = datetime("started_at").uniqueIndex()
    val endedAt = datetime("ended_at").uniqueIndex().nullable()
    val days = jsonb("days", String.serializer().set).default(setOf())
    val conditions = embedded<Conditions>(ConditionsTable)
    val location = reference("location", Locations).nullable()
    val owner = uuid("owner")
    val status = customEnumeration(
        "status",
        "event_status",
        { value -> Status.valueOf(value as String) },
        { PGEnum("event_status", it) }).default(Status.OPENED)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").nullable()
}
