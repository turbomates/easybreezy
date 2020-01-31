package io.easybreezy

import com.jdiazcano.cfg4k.providers.getOrNull
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.easybreezy.application.SystemConfiguration
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.api.ExposedSavepoint
import org.jetbrains.exposed.sql.transactions.DEFAULT_REPETITION_ATTEMPTS
import org.jetbrains.exposed.sql.transactions.TransactionInterface
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import javax.sql.DataSource

class ExposedTestTransactionManager(
    private val db: Database,
    @Volatile override var defaultIsolationLevel: Int,
    @Volatile override var defaultRepetitionAttempts: Int
) : TransactionManager {
    var transaction: Transaction? = null

    override fun currentOrNull(): Transaction? {
        return transaction
    }

    override fun newTransaction(isolation: Int, outerTransaction: Transaction?): Transaction {
        transaction = Transaction(
            TestTransaction(
                db = db,
                transactionIsolation = defaultIsolationLevel,
                manager = this,
                outerTransaction = outerTransaction ?: transaction
            )
        )
        return transaction!!
    }

    private class TestTransaction(
        override val db: Database,
        override val transactionIsolation: Int,
        val manager: ExposedTestTransactionManager,
        override val outerTransaction: Transaction?
    ) : TransactionInterface {

        override val connection = outerTransaction?.connection ?: db.connector().apply {
            autoCommit = false
            transactionIsolation = this@TestTransaction.transactionIsolation
        }

        private val useSavePoints = outerTransaction != null && db.useNestedTransactions
        private var savepoint: ExposedSavepoint? = if (useSavePoints) {
            connection.setSavepoint(savepointName)
        } else null

        override fun commit() {
            if (!useSavePoints) {
                connection.commit()
            }
        }

        override fun rollback() {
            if (!connection.isClosed) {
                if (useSavePoints && savepoint != null) {
                    connection.rollback(savepoint!!)
                    savepoint = connection.setSavepoint(savepointName)
                } else {
                    connection.rollback()
                }
            }
        }

        override fun close() {
            try {
                if (!useSavePoints) {
                    connection.close()
                } else {
                    savepoint?.let {
                        connection.releaseSavepoint(it)
                        savepoint = null
                    }
                }
            } finally {
                manager.transaction = outerTransaction
            }
        }

        private val savepointName: String
            get() {
                var nestedLevel = 0
                var currenTransaction = outerTransaction
                while (currenTransaction != null) {
                    nestedLevel++
                    currenTransaction = currenTransaction.outerTransaction
                }
                return "Exposed_savepoint_$nestedLevel"
            }
    }
}

fun <T> rollbackTransaction(db: Database? = null, statement: Transaction.() -> T): T {
    return transaction(db) { val result = statement(); rollback(); result }
}

internal val lookupDataSource: () -> DataSource = {
    val hkConfig = HikariConfig().apply {
        connectionTestQuery = "select 1"
        jdbcUrl = SystemConfiguration.getOrNull("easybreezy.jdbc.url")
        username = SystemConfiguration.getOrNull("easybreezy.jdbc.user")
        password = SystemConfiguration.getOrNull("easybreezy.jdbc.password")
        transactionIsolation = "TRANSACTION_READ_COMMITTED"
    }

    HikariDataSource(hkConfig)
}

abstract class TestDataSourceBase(private val ds: DataSource) : DataSource by ds

object TestDataSource : TestDataSourceBase(lookupDataSource())

internal val testDatabase by lazy {
    Database.connect(
        TestDataSource,
        manager = { database ->
            database.useNestedTransactions = true
            ExposedTestTransactionManager(
                database,
                Connection.TRANSACTION_READ_COMMITTED,
                DEFAULT_REPETITION_ATTEMPTS
            )
        })
}
