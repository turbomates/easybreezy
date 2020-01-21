package io.easybreezy.infrastructure.event

import io.easybreezy.infrastructure.exposed.type.jsonb
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.batchInsert
import java.util.*

object EventsDatabaseAccess {
    fun save(events: List<Event>) {
        Events.batchInsert(events) { event ->
            this[Events.event] = EventWrapper(event)
        }
    }

    fun load(): List<Event> {
        return emptyList()
    }

    fun update(id: UUID) {

    }
}

private object Events : UUIDTable("domain_events") {
    val event = jsonb("event", EventWrapper.serializer())
    val publishedAt = datetime("published_at").nullable()
    val createdAt = datetime("created_at").nullable()
}
