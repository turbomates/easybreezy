package io.easybreezy.hr.infrastructure

import io.easybreezy.hr.model.event.Event
import io.easybreezy.hr.model.event.EventId
import io.easybreezy.hr.model.event.Events
import io.ktor.features.NotFoundException

class EventRepository : Event.Repository() {
    fun getOne(event: EventId): Event {
        return find(event) ?: throw NotFoundException("Event with id $event not found")
    }

    private fun find(event: EventId): Event? {
        return find { Events.id eq event }.firstOrNull()
    }
}
