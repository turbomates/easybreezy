package io.easybreezy.infrastructure.event

import io.easybreezy.infrastructure.exposed.type.jsonb
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime
import java.util.*

class EventsDatabaseAccess(private val database: Database) {
    fun load(): List<Pair<UUID, Event>> {
        return transaction(database) {
            Events.select { Events.publishedAt.isNull() }.map {
                it[Events.id].value to it[Events.event].event
            }
        }
    }

    fun publish(id: UUID) {
        transaction(database) {
            Events.update({ Events.id eq id }) {
                it[publishedAt] = LocalDateTime.now()
            }
        }
    }
}

internal fun EventStore.save() {
    Events.batchInsert(this.raiseEvents()) { event ->
        this[Events.event] = EventWrapper(event)
    }
}

private object Events : UUIDTable("domain_events") {
    val event = jsonb("event", EventWrapper.serializer())
    val publishedAt = datetime("published_at").nullable()
    val createdAt = datetime("created_at").default(LocalDateTime.now())
}
