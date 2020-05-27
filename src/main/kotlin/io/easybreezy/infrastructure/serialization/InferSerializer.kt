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
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.full.starProjectedType

fun Collection<*>.elementSerializer(): KSerializer<*> {
    val serializers = mapNotNull { value ->
        value?.let { resolveSerializer(it) }
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

fun resolveSerializer(value: Any): KSerializer<*> {
    return when (value) {
        is JsonElement -> JsonElementSerializer
        is List<*> -> ListSerializer(value.elementSerializer())
        is Set<*> -> SetSerializer(value.elementSerializer())
        is Map<*, *> -> MapSerializer(value.keys.elementSerializer(), value.values.elementSerializer())
        is Map.Entry<*, *> -> MapEntrySerializer(
            resolveSerializer(value.key ?: error("Map.Entry(null, ...) is not supported")),
            resolveSerializer(value.value ?: error("Map.Entry(..., null) is not supported)"))
        )
        is Array<*> -> {
            val componentType = value.javaClass.componentType.kotlin.starProjectedType
            val componentClass =
                componentType.classifier as? KClass<*> ?: error("Unsupported component type $componentType")
            @Suppress("UNCHECKED_CAST")
            ArraySerializer(
                componentClass as KClass<Any>,
                serializer(componentType) as KSerializer<Any>
            )
        }
        is LocalDate -> LocalDateSerializer
        is LocalDateTime -> LocalDateTimeSerializer
        is UUID -> UUIDSerializer
        else -> value::class.serializer()
    }
}
