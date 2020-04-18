package io.easybreezy.infrastructure.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapEntrySerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonElementSerializer
import kotlinx.serialization.serializer
import kotlin.reflect.KClass
import kotlin.reflect.full.starProjectedType

fun Collection<*>.elementSerializer(): KSerializer<*> {
    val serializers = mapNotNull { value ->
        value?.let { serializerForSending(it) }
    }.distinctBy { it.descriptor.serialName }

    if (serializers.size > 1) {
        error(
            "Serializing collections of different element types is not yet supported. " +
                "Selected serializers: ${serializers.map { it.descriptor.serialName }}"
        )
    }

    val selected: KSerializer<*> = serializers.singleOrNull() ?: String::class.serializer()
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

fun serializerForSending(value: Any): KSerializer<*> {
    if (value is JsonElement) {
        return JsonElementSerializer
    }
    if (value is List<*>) {
        return ListSerializer(value.elementSerializer())
    }
    if (value is Set<*>) {
        return SetSerializer(value.elementSerializer())
    }
    if (value is Map<*, *>) {
        return MapSerializer(value.keys.elementSerializer(), value.values.elementSerializer())
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
        return ArraySerializer(
            componentClass as KClass<Any>,
            serializer(componentType) as KSerializer<Any>
        )
    }
    return value::class.serializer()
}
