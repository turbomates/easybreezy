package io.easybreezy.infrastructure.event

interface EventsSubscriber {
    fun subscribers(): List<EventSubscriberItem<out Event>>
    data class EventSubscriberItem<T : Event>(val key: Event.Key<T>, val subscriber: EventSubscriber<T>)

    infix fun <T : Event, A : Event.Key<T>, B : EventSubscriber<T>> A.to(that: B): EventSubscriberItem<T> =
        EventSubscriberItem(this, that)
}

interface EventSubscriber<T : Event> {
    operator fun invoke(event: T)
}

class EventSystem {
    private val subscribers: MutableMap<Event.Key<out Event>, MutableList<EventSubscriber<out Event>>> =
        mutableMapOf()

    fun subscribe(subscriber: EventsSubscriber) {
        subscriber.subscribers().forEach {
            addToMap(it.key, it.subscriber)
        }
    }

    fun <T : Event> subscribe(key: Event.Key<T>, subscriber: EventSubscriber<T>) {
        addToMap(key, subscriber)
    }

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
//class GuiceSubscriber(val test: String, val test1: KClass<EventSubscriber>) : EventSubscriber {
//    override fun subscribers(): List<Pair<KClass<out Event>, Subscriber<out Event>>> {
//        val subscriber = PlayerSubscriber("string")
//        return listOf(Activated::class to object : Subscriber<Activated> {
//            override fun invoke(event: Activated) {
//                test.length
//            }
//        })
//    }
//
//}