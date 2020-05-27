@file:UseSerializers(UUIDSerializer::class, LocalDateTimeSerializer::class)

package io.easybreezy.hr.application.event.queryobject

import io.easybreezy.hr.application.location.queryobject.Location
import io.easybreezy.hr.application.location.queryobject.toLocation
import io.easybreezy.hr.model.event.ConditionsTable
import io.easybreezy.hr.model.event.EventId
import io.easybreezy.hr.model.event.Events
import io.easybreezy.hr.model.event.EmployeeId
import io.easybreezy.hr.model.event.Participants
import io.easybreezy.hr.model.event.Status
import io.easybreezy.hr.model.event.VisitStatus
import io.easybreezy.hr.model.location.Locations
import io.easybreezy.infrastructure.query.ContinuousList
import io.easybreezy.infrastructure.query.PagingParameters
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.query.toContinuousList
import io.easybreezy.infrastructure.serialization.LocalDateTimeSerializer
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import io.easybreezy.user.model.EmailTable
import io.easybreezy.user.model.NameTable
import io.easybreezy.user.model.Users
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.time.LocalDateTime
import java.util.UUID
import io.easybreezy.hr.model.event.Owner as OwnerId

class EventQO(private val event: EventId) : QueryObject<Event> {
    override suspend fun getData() =
        Events
            .leftJoin(Locations)
            .leftJoin(Participants)
            .join(Users, JoinType.INNER, additionalConstraint = {
                Users.id eq Participants.employee or (Users.id eq Events.owner)
            })
            .selectAll()
            .andWhere { Events.id eq event }
            .toEventJoined()
}

class EventsQO(private val viewer: UUID, private val paging: PagingParameters) : QueryObject<ContinuousList<Event>> {
    override suspend fun getData() =
        Events
            .leftJoin(Locations)
            .join(Users, JoinType.INNER, additionalConstraint = { Users.id eq Events.owner })
            .selectAll()
            .andWhere { Events.conditions[ConditionsTable.isPrivate] eq false or (Events.owner eq viewer) }
            .toContinuousList(paging, ResultRow::toEventWithOwner)
}

class IsEventOwner(private val event: EventId, private val owner: OwnerId) : QueryObject<Boolean> {
    override suspend fun getData(): Boolean {
        return Events.select { Events.id eq event and (Events.owner eq owner) }.count() > 0
    }
}

class CanViewEvent(private val event: EventId, private val employee: UUID) : QueryObject<Boolean> {
    override suspend fun getData(): Boolean {
        return (Events innerJoin Participants).select {
            Events.id eq event and (
                Events.owner eq employee or (Participants.employee eq employee) or (Events.conditions[ConditionsTable.isPrivate] eq false)
                )
        }.count() > 0
    }
}

private fun Iterable<ResultRow>.toEventJoined(): Event {
    return fold(mutableMapOf<UUID, Event>()) { map, resultRow ->
        val event = resultRow.toEvent()
        val current = map.getOrDefault(event.id, event)
        val owner = resultRow[Events.owner]

        val userId = resultRow[Users.id]
        if (userId.value == owner) current.owner = resultRow.toOwner()
        else current.participants.add(resultRow.toParticipant())

        map[event.id] = current
        map
    }.values.toList().first()
}

private fun ResultRow.toEvent(): Event {
    return Event(
        id = this[Events.id].value,
        name = this[Events.name],
        description = this[Events.description],
        startedAt = this[Events.startedAt],
        endedAt = this[Events.endedAt],
        conditions = this.toConditions(),
        location = this.getOrNull(Locations.id)?.let { this.toLocation() },
        status = this[Events.status],
        createdAt = this[Events.createdAt],
        updatedAt = this[Events.updatedAt],
        days = this[Events.days]
    )
}

private fun ResultRow.toEventWithOwner(): Event {
    val event = this.toEvent()
    event.owner = this.toOwner()
    return event
}

private fun ResultRow.toOwner(): Owner {
    return Owner(
        email = this[Users.email[EmailTable.email]],
        firstName = this[Users.name[NameTable.firstName]],
        lastName = this[Users.name[NameTable.lastName]]
    )
}

private fun ResultRow.toParticipant() = Participant(
    id = this[Participants.employee],
    firstName = this[Users.name[NameTable.firstName]],
    lastName = this[Users.name[NameTable.lastName]],
    visitStatus = this[Participants.visitStatus]
)

private fun ResultRow.toConditions() = Conditions(
    isPrivate = this[Events.conditions[ConditionsTable.isPrivate]],
    isFreeEntry = this[Events.conditions[ConditionsTable.isFreeEntry]],
    isRepeatable = this[Events.conditions[ConditionsTable.isRepeatable]]
)

@Serializable
data class Event(
    val id: EventId,
    val name: String,
    val description: String?,
    val startedAt: LocalDateTime?,
    val endedAt: LocalDateTime?,
    val conditions: Conditions,
    val location: Location?,
    val status: Status,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val participants: MutableList<Participant> = mutableListOf(),
    val days: Set<String>
) {
    lateinit var owner: Owner
}

@Serializable
data class Conditions(
    val isPrivate: Boolean?,
    val isFreeEntry: Boolean?,
    val isRepeatable: Boolean?
)

@Serializable
data class Participant(
    val id: EmployeeId,
    val firstName: String?,
    val lastName: String?,
    val visitStatus: VisitStatus
)

@Serializable
data class Owner(
    val email: String?,
    val firstName: String?,
    val lastName: String?
)
