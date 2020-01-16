package io.easybreezy.infrastructure.domain

import io.easybreezy.infrastructure.event.Event

abstract class AggregateRoot {
    @Transient
    private var events: MutableList<Event> = mutableListOf()

    fun releaseEvents(): Iterator<Event> {
        return events.iterator()
    }

    final fun addEvent(event: Event) {
        events.add(event)
    }
}
