package io.easybreezy.application.db.test

import java.sql.Connection

abstract class TestConnectionBase(private val connection: Connection) : Connection by connection

object TestConnection : TestConnectionBase(lookupDataSource().connection) {
    internal var allowCommit: Boolean = false

    override fun close() {
        // Do nothing
    }

    override fun commit() {
        if (allowCommit) {
            println("COMMITTED")
            super.commit()
        }
    }
}
