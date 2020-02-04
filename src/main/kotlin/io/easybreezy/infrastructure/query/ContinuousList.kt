package io.easybreezy.infrastructure.query

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.internal.HashMapSerializer
import kotlinx.serialization.internal.HashSetSerializer
import kotlinx.serialization.internal.MapEntrySerializer
import kotlinx.serialization.internal.ReferenceArraySerializer
import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.internal.nullable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonElementSerializer
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonOutput
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import kotlin.reflect.KClass
import kotlin.reflect.full.starProjectedType

fun <T> List<T>.toContinuousList(pageSize: Int, currentPage: Int): ContinuousList<T> {
    val hasMore = size > pageSize
    val list = take(pageSize)
    return ContinuousList(list, pageSize, currentPage, hasMore)
}

class ContinuousList<T>(
    val data: List<T>,
    val pageSize: Int,
    val currentPage: Int,
    val hasMore: Boolean = false
)

object ContinuousListSerializer : KSerializer<ContinuousList<*>> {
    override val descriptor: SerialDescriptor = SerialClassDescImpl("ContinuousListDescriptor")
    override fun serialize(encoder: Encoder, obj: ContinuousList<*>) {
        val output = encoder as? JsonOutput ?: throw SerializationException("This class can be saved only by Json")
        val tree = JsonObject(
            mapOf(
                "pageSize" to JsonPrimitive(obj.pageSize),
                "currentPage" to JsonPrimitive(obj.currentPage),
                "hasMore" to JsonPrimitive(obj.hasMore),
                "data" to output.json.toJson(obj.data.elementSerializer().list as KSerializer<Any>, obj.data)
            )
        )
        output.encodeJson(tree)
    }

    override fun deserialize(decoder: Decoder): ContinuousList<*> {
        throw NotImplementedError()
    }
}

private fun Collection<*>.elementSerializer(): KSerializer<*> {
    val serializers = mapNotNull { value ->
        value?.let { serializerForSending(it) }
    }.distinctBy { it.descriptor.name }

    if (serializers.size > 1) {
        error(
            "Serializing collections of different element types is not yet supported. " +
                    "Selected serializers: ${serializers.map { it.descriptor.name }}"
        )
    }

    val selected: KSerializer<*> = serializers.singleOrNull() ?: String.serializer()
    if (selected.descriptor.isNullable) {
        return selected
    }

    @Suppress("UNCHECKED_CAST")
    selected as KSerializer<Any>

    if (any { it == null }) {
        return selected.nullable
    }

    return selected
}

private fun serializerForSending(value: Any): KSerializer<*> {
    if (value is JsonElement) {
        return JsonElementSerializer
    }
    if (value is List<*>) {
        return ArrayListSerializer(value.elementSerializer())
    }
    if (value is Set<*>) {
        return HashSetSerializer(value.elementSerializer())
    }
    if (value is Map<*, *>) {
        return HashMapSerializer(value.keys.elementSerializer(), value.values.elementSerializer())
    }
    if (value is Map.Entry<*, *>) {
        return MapEntrySerializer(
            serializerForSending(value.key ?: error("Map.Entry(null, ...) is not supported")),
            serializerForSending(value.value ?: error("Map.Entry(..., null) is not supported)"))
        )
    }
    if (value is Array<*>) {
        val componentType = value.javaClass.componentType.kotlin.starProjectedType
        val componentClass =
            componentType.classifier as? KClass<*> ?: error("Unsupported component type $componentType")
        @Suppress("UNCHECKED_CAST")
        return ReferenceArraySerializer(
            componentClass as KClass<Any>,
            serializer(componentType) as KSerializer<Any>
        )
    }
    return value::class.serializer()
}
