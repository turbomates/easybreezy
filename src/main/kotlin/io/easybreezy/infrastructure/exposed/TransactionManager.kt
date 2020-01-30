package io.easybreezy.infrastructure.exposed

import io.easybreezy.infrastructure.event.EventStore
import io.easybreezy.infrastructure.event.save
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.GlobalStatementInterceptor
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.transactionScope

val Transaction.events: EventStore by transactionScope { EventStore() }

class TransactionManager(private val database: Database) {
    suspend operator fun <T> invoke(statement: Transaction.() -> T): T {
//        return newSuspendedTransaction(Dispatchers.IO, database, statement)
        return transaction(database, statement)
    }
}

class EventListenerInterceptor() : GlobalStatementInterceptor {
    override fun beforeCommit(transaction: Transaction) {
        transaction.events.save()
    }
}
