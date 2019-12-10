package io.easybreezy.application.db.test

import org.jooq.impl.DSL
import java.sql.Connection

enum class SweepingStrategy {
    TRANSACTION_ROLLBACK {
        override fun execute(connection: TestConnection, block: (Connection) -> Unit) {
            val originAllowCommit = connection.allowCommit

            connection.allowCommit = false
            val sp = connection.setSavepoint()

            try {
                block(connection)
            } finally {
                connection.allowCommit = originAllowCommit
                runCatching { connection.rollback(sp) }
            }
        }
    },

    TRUNCATE {
        override fun execute(connection: TestConnection, block: (Connection) -> Unit) {
            val originAllowCommit = connection.allowCommit
            connection.allowCommit = true

            try {
                block(connection)
            } finally {
                try {
                    DSL.using(connection).use {
                        val qualifiedTableNames = mutableListOf<String>()

                        for (schema in listOf("public")) {
                            val result = it.fetch("select table_name from information_schema.tables where table_schema = '$schema' and table_type = 'BASE TABLE';")
                            val tables = result.map { row -> row.getValue("table_name", String::class.java) }
                            qualifiedTableNames.addAll(tables.map { table -> "$schema.$table" })
                        }

                        val sql = "truncate ${qualifiedTableNames.joinToString()} cascade;"
                        it.query(sql).execute()
                        connection.commit()
                    }
                } finally {
                    connection.allowCommit = originAllowCommit
                }
            }
        }
    };

    abstract fun execute(connection: TestConnection, block: (Connection) -> Unit)
}
