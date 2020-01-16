package io.easybreezy.infrastructure.event

import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
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
internal data class EventWrapper(val event: Event)

internal object EventWrapperSerializer : KSerializer<EventWrapper> {
    override val descriptor: SerialDescriptor = object : SerialClassDescImpl("EventDescriptor") {
        init {
            addElement("type") // req will have index 0
            addElement("body") // res will have index 1
        }
    }

    override fun deserialize(decoder: Decoder): EventWrapper {
        val dec: CompositeDecoder = decoder.beginStructure(descriptor)
        var type: KClass<Event>? = null // consider using flags or bit mask if you
        var body: Event? = null // need to read nullable non-optional properties
        loop@ while (true) {
            when (val i = dec.decodeElementIndex(descriptor)) {
                CompositeDecoder.READ_DONE -> break@loop
                0 -> type = Class.forName(dec.decodeStringElement(descriptor, i)).kotlin as KClass<Event>
                1 -> if (type != null) {
                    body = dec.decodeSerializableElement(descriptor, i, type.serializer())
                }
                else -> throw SerializationException("Unknown index $i")
            }
        }
        dec.endStructure(descriptor)

        return EventWrapper(body!!)
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
