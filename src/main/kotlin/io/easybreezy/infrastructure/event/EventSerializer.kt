package io.easybreezy.infrastructure.event

import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.json.*
import kotlin.reflect.KClass

object EventSerializer {
    private val json: Json
        get() {
            return Json(configuration = JsonConfiguration(useArrayPolymorphism = true))
        }

    fun serialize(event: Event): String {
        return json.stringify(EventWrapper(event))
    }

    fun deserialize(event: String): Event {
        val jsonElement = json.parseJson(event)
        val wrapper = json.fromJson(EventWrapper.serializer(), jsonElement)

        return wrapper.event
    }
}

@Serializable(with = EventWrapperSerializer::class)
internal data class EventWrapper(@Polymorphic val event: Event)

internal object EventWrapperSerializer : KSerializer<EventWrapper> {
    override val descriptor: SerialDescriptor = SerialClassDescImpl("EventDescriptor")
    override fun deserialize(decoder: Decoder): EventWrapper {
        val input = decoder as? JsonInput ?: throw SerializationException("This class can be loaded only by Json")
        val tree = input.decodeJson() as? JsonObject ?: throw SerializationException("Expected JsonObject")
        var type: KClass<Event> = Class.forName(tree.getPrimitive("type").content).kotlin as KClass<Event>
        var body: Event = input.json.fromJson(type.serializer(), tree.getObject("body"))

        return EventWrapper(body)
    }

    override fun serialize(encoder: Encoder, obj: EventWrapper) {
        val output = encoder as? JsonOutput ?: throw SerializationException("This class can be saved only by Json")
        val tree = JsonObject(
            mapOf(
                "type" to JsonLiteral(obj.event::class.qualifiedName!!),
                "body" to output.json.toJson(obj.event::class.serializer() as KSerializer<Event>, obj.event)
            )
        )
        output.encodeJson(tree)
    }
}