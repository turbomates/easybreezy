package io.easybreezy.infrastructure.exposed.dao

import io.easybreezy.infrastructure.events.Event
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityID

open class AggregateRoot<ID : Comparable<ID>>(id: EntityID<ID>) : Entity<ID>(id) {
    private var events: MutableList<Event> = mutableListOf()

    fun releaseEvents(): Iterator<Event> {
        return events.iterator()
    }

    fun addEvent(event: Event) {
        events.add(event)
    }
}