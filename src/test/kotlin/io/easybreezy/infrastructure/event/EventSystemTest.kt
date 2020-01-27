package io.easybreezy.infrastructure.event

import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class EventSystemTest {
    @Test
    fun testInvokeSubscriber() {
        val store = EventSubscribers()
        var i = 0
        store.subscribe(TestEventForSubscriber, object : EventSubscriber<TestEventForSubscriber> {
            override fun invoke(event: TestEventForSubscriber) {
                i++
            }
        })
        val event = TestEventForSubscriber("test_value")
        store.call(event)
        assertEquals(1, i)
    }

    @Test
    fun testInvokeSubscribers() {
        val store = EventSubscribers()
        val subscriber = TestSubscriber()
        store.subscribe(subscriber)
        val event = TestEventForSubscriber("test_value")
        store.call(event)
        assertEquals(1, subscriber.i)
    }
}

@Serializable
data class TestEventForSubscriber(val test: String) : Event {
    override val key
        get() = Key

    companion object Key : Event.Key<TestEventForSubscriber>
}

class TestSubscriber : EventsSubscriber {
    var i = 0
    override fun subscribers(): List<EventsSubscriber.EventSubscriberItem<out Event>> {
        return listOf(TestEventForSubscriber to object : EventSubscriber<TestEventForSubscriber> {
            override fun invoke(event: TestEventForSubscriber) {
                i++
            }
        })
    }
}
