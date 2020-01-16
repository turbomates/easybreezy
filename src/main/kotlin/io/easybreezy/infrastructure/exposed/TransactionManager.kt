package io.easybreezy.infrastructure.exposed

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction

fun <T> transaction(db: Database? = null, statement: Transaction.() -> T): T {
    return org.jetbrains.exposed.sql.transactions.transaction(db, statement)
}

class EventListenerInterceptor() {

}