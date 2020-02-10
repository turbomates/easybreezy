package io.easybreezy.infrastructure.event

import kotlin.reflect.full.companionObjectInstance

interface EventsSubscriber {
    fun subscribers(): List<EventSubscriberItem<out Event>>
    data class EventSubscriberItem<T : Event>(val key: Event.Key<T>, val subscriber: EventSubscriber<T>)

    infix fun <T : Event, A : Event.Key<T>, B : EventSubscriber<T>> A.to(that: B): EventSubscriberItem<T> =
        EventSubscriberItem(this, that)
}

interface EventSubscriber<T : Event> {
    operator fun invoke(event: T)
}

class EventSubscribers {
    private val subscribers: MutableMap<Event.Key<out Event>, MutableList<EventSubscriber<out Event>>> =
        mutableMapOf()

    fun subscribe(subscriber: EventsSubscriber) {
        subscriber.subscribers().forEach {
            addToMap(it.key, it.subscriber)
        }
    }
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Event> subscribe(subscriber: EventSubscriber<T>) {
        val key = T::class.companionObjectInstance as? Event.Key<T>
        key?.let { subscribe(it, subscriber) }
        if (key == null) {
            throw InvalidKeyException("wrong subscriber")
        }
    }

    fun <T : Event> subscribe(key: Event.Key<T>, subscriber: EventSubscriber<T>) {
        addToMap(key, subscriber)
    }
    @Suppress("UNCHECKED_CAST")
    fun <T : Event> call(event: T) {
        val subscribers = subscribers.getOrDefault(event.key, mutableListOf()) as MutableList<EventSubscriber<T>>
        subscribers.forEach {
            it(event)
        }
    }

    private fun addToMap(key: Event.Key<out Event>, subscriber: EventSubscriber<out Event>) {
        val list = subscribers.getOrPut(key) {
            mutableListOf()
        }
        list.add(subscriber)
    }
}
