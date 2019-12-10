package io.easybreezy.infrastructure.gson

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import kotlin.reflect.full.memberProperties

open class AbstractTypeAdapter<T : Any> : JsonSerializer<T>, JsonDeserializer<T> {
    override fun serialize(src: T, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val result = JsonObject()
        result.add("type", JsonPrimitive(src::class.java.name))
        val properties = JsonObject()
        src.javaClass.kotlin.memberProperties.forEach {
            properties.add(it.name, context.serialize(it.get(src)))
        }
        result.add("properties", properties)

        return result
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): T {
        val jsonObject = json.asJsonObject
        if (jsonObject.has("type")) {
            val type = jsonObject.get("type").asString
            val element = jsonObject.get("properties")

            return context.deserialize(element, Class.forName(type))
        }

        return Gson().fromJson(json, typeOfT)
    }
}
