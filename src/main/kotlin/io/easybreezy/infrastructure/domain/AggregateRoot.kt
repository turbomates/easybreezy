package io.easybreezy.infrastructure.domain

import io.easybreezy.infrastructure.events.Event
import javax.persistence.MappedSuperclass
import javax.persistence.Transient

@MappedSuperclass
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
