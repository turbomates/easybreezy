package io.easybreezy.infrastructure.events

import com.google.gson.Gson
import com.google.gson.JsonElement
import io.easybreezy.infrastructure.gson.AbstractTypeAdapter

class EventSerializer {
    private val gson: Gson = Gson()
        .newBuilder()
        .registerTypeAdapter(Event::class.java, AbstractTypeAdapter<Event>())
        .create()

    fun serialize(event: Event): JsonElement {
        return gson.toJsonTree(event, Event::class.java)
    }

    fun deserialize(json: String): Event {
        return gson.fromJson(json, Event::class.java)
    }

    fun deserialize(json: JsonElement): Event {
        return gson.fromJson(json, Event::class.java)
    }
}
