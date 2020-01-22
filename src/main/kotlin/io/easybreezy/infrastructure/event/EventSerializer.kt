package io.easybreezy.infrastructure.event

import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.JsonObject
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
    override val descriptor: SerialDescriptor = object : SerialClassDescImpl("EventDescriptor") {
        init {
            addElement("type") // req will have index 0
            addElement("body") // res will have index 1
        }
    }

    override fun deserialize(decoder: Decoder): EventWrapper {
        val input = decoder as? JsonInput ?: throw SerializationException("This class can be loaded only by Json")
        val tree = input.decodeJson() as? JsonObject ?: throw SerializationException("Expected JsonObject")
        var type: KClass<Event> = Class.forName(tree.getPrimitive("type").content).kotlin as KClass<Event>
        var body: Event = input.json.fromJson(type.serializer(), tree.getObject("body"))

        return EventWrapper(body)
    }

    override fun serialize(encoder: Encoder, obj: EventWrapper) {
        val compositeOutput = encoder.beginStructure(descriptor)
        compositeOutput.encodeStringElement(descriptor, 0, obj.event::class.qualifiedName!!)
        compositeOutput.encodeSerializableElement(
            descriptor,
            1,
            obj.event::class.serializer() as KSerializer<Event>,
            obj.event
        )
        compositeOutput.endStructure(descriptor)
    }
}
