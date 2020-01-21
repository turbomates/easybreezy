package io.easybreezy.infrastructure.event

class EventStore {
    private val events: MutableList<Event> = mutableListOf()
    fun addEvent(event: Event) {
        events.add(event)
    }

    fun raiseEvents(): List<Event> {
        val result = events;
        events.clear()
        return result
    }
}

