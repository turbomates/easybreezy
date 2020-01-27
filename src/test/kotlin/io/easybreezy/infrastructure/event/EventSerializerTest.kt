package io.easybreezy.infrastructure.event

import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class EventSerializerTest {
    @Test
    fun testSerialize() {
        val event = TestEvent("test")
        val result = EventSerializer.serialize(event)
        assert(result.contains("TestEvent"))
        assert(result.contains("test"))
    }

    @Test
    fun testDeserialize() {
        val event = TestEvent("test_value")
        val result = EventSerializer.serialize(event)
        val deserializeEvent = EventSerializer.deserialize(result)
        assert(deserializeEvent is TestEvent)
        assertEquals(event.test, (deserializeEvent as TestEvent).test)
    }
}

@Serializable
data class TestEvent(val test: String) : Event {
    override val key
        get() = Companion

    companion object : Event.Key<TestEvent>
}
