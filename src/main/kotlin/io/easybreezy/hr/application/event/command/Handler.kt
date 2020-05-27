package io.easybreezy.hr.application.event.command

import com.google.inject.Inject
import io.easybreezy.hr.infrastructure.EventRepository
import io.easybreezy.hr.infrastructure.LocationRepository
import io.easybreezy.hr.model.event.Conditions
import io.easybreezy.hr.model.event.Event
import io.easybreezy.hr.model.event.EventId
import io.easybreezy.hr.model.event.VisitStatus
import io.easybreezy.hr.model.event.WeekDays
import io.easybreezy.infrastructure.exposed.TransactionManager

class Handler @Inject constructor(
    private val transaction: TransactionManager,
    private val repository: EventRepository
) {

    suspend fun openEvent(command: Open) = transaction {
        val conditions = Conditions.create(command.isPrivate, command.isFreeEntry, command.isRepeatable)
        val event = Event.open(command.name, command.startedAt, command.owner, conditions)
        command.description?.let { event.changeDescription(it) }
        command.location?.let { event.assignLocation(LocationRepository().getOne(it)) }
        command.endedAt?.let { event.specifyEndTime(it) }
        command.days?.let { days -> event.holdOnDays(days.map { WeekDays.valueOf(it) }.toSet()) }
    }

    suspend fun updateEventDetails(command: UpdateDetails) = transaction {
        val event = repository.getOne(command.id)
        command.name?.let { event.changeName(it) }
        command.description?.let { event.changeDescription(it) }
        command.location?.let { event.assignLocation(LocationRepository().getOne(command.location)) }
        command.startedAt?.let { event.specifyStartTime(it) }
        command.endedAt?.let { event.specifyEndTime(it) }
        command.days?.let { days -> event.holdOnDays(days.map { WeekDays.valueOf(it) }.toSet()) }
    }

    suspend fun changeEventConditions(command: ChangeConditions) = transaction {
        val event = repository.getOne(command.event)
        command.isPrivate?.let { if (it) event.makePrivate() else event.makePublic() }
        command.isFreeEntry?.let { if (it) event.openEntry() else event.closeEntry() }
        command.isRepeatable?.let { if (it) event.makeRepeatable() else event.makeDoneOnce() }
    }

    suspend fun cancelEvent(eventId: EventId) = transaction {
        repository.getOne(eventId).cancel()
    }

    suspend fun addParticipants(command: AddParticipants) = transaction {
        val event = repository.getOne(command.event)
        event.addParticipants(command.participants)
    }

    suspend fun enterEvent(command: Enter) = transaction {
        val event = repository.getOne(command.event)
        event.enter(command.employeeId)
    }

    suspend fun changeParticipantVisitStatus(command: ChangeVisitStatus) = transaction {
        val event = repository.getOne(command.event)
        event.changeParticipantVisitStatus(command.employeeId, VisitStatus.valueOf(command.status))
    }
}
