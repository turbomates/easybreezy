package io.easybreezy.infrastructure.events

import org.jooq.DSLContext
import java.sql.Connection

class DatabasePublisher(private val access: EventDatabaseGateway) : Publisher {
    override fun publish(event: Event): Boolean {
        return access.publish(event)
    }
}

fun Connection.publishEvent(event: Event): Boolean {
    val publisher = DatabasePublisher(EventDatabaseGateway(this, EventSerializer()))
    return publisher.publish(event)
}

fun DSLContext.publishEvent(event: Event): Boolean {
    return connectionResult { DatabasePublisher(EventDatabaseGateway(it, EventSerializer())).publish(event) }
}
