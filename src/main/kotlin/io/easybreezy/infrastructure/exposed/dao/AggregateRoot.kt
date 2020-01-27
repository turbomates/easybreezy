package io.easybreezy.infrastructure.exposed.dao

import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.exposed.events
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.TransactionManager

open class AggregateRoot<ID : Comparable<ID>>(id: EntityID<ID>) : Entity<ID>(id) {
    private var store = TransactionManager.current().events
    fun addEvent(event: Event) {
        store.addEvent(event)
    }
}
