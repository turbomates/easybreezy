package io.easybreezy.infrastructure.hibernate

import io.easybreezy.infrastructure.domain.AggregateRoot
import io.easybreezy.infrastructure.events.publishEvent
import org.hibernate.EmptyInterceptor
import org.slf4j.LoggerFactory
import java.sql.Connection

class HibernateInterceptor constructor(private val connection: Connection) : EmptyInterceptor() {
    private val logger by lazy { LoggerFactory.getLogger(javaClass) }
    override fun postFlush(entities: Iterator<*>) {
        for (entity in entities) {
            if (entity is AggregateRoot) {
                for (event in entity.releaseEvents()) {
                    connection.publishEvent(event)
                }
            }
        }
    }
}
