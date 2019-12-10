package io.easybreezy.infrastructure.events

import com.google.gson.JsonElement
import io.easybreezy.tables.DomainEvents.DOMAIN_EVENTS
import org.jooq.Record
import org.jooq.RecordMapper
import org.jooq.impl.DSL
import java.sql.Connection
import java.sql.Timestamp

class EventDatabaseGateway(private val connection: Connection, private val serializer: EventSerializer) {
    private val mapper = EventRecordMapper(serializer)
    fun getUnpublished(): List<Event> {
        return DSL.using(connection)
            .select(DOMAIN_EVENTS.ID)
            .select(DOMAIN_EVENTS.EVENT)
            .from(DOMAIN_EVENTS)
            .where(DOMAIN_EVENTS.PUBLISHED_AT.isNull)
            .fetch(mapper)
    }

    fun publish(event: Event): Boolean {
        return DSL.using(connection)
            .insertInto(DOMAIN_EVENTS,
                DOMAIN_EVENTS.ID, DOMAIN_EVENTS.EVENT, DOMAIN_EVENTS.CREATED_AT)
            .values(event.sourceId, serializer.serialize(event), Timestamp(System.currentTimeMillis()))
            .execute() == 1
    }

    fun markAsPublished(event: Event): Boolean {
        return DSL.using(connection)
            .update(DOMAIN_EVENTS)
            .set(DOMAIN_EVENTS.PUBLISHED_AT, Timestamp(System.currentTimeMillis()))
            .where(DOMAIN_EVENTS.ID.eq(event.sourceId))
            .execute() == 1
    }
}

private class EventRecordMapper(private val serializer: EventSerializer) : RecordMapper<Record, Event> {
    override fun map(record: Record): Event {
        val json: JsonElement = record.getValue(DOMAIN_EVENTS.EVENT)
        return serializer.deserialize(json)
    }
}
