package io.easybreezy.infrastructure.event

class EventStore {
    private val events: MutableList<Event> = mutableListOf()
    fun addEvent(event: Event) {
        events.add(event)
    }

    fun raiseEvents(): Sequence<Event> = sequence {
        events.forEach {
            yield(it)
        }
        events.clear()
    }
}
