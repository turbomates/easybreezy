package io.easybreezy.migrations.extensions

import org.flywaydb.core.api.migration.Context
import java.sql.ResultSet

fun Context.execute(sql: String) {
    connection.createStatement().use { statement ->
        statement.execute(sql)
    }
}

fun Context.query(sql: String): ResultSet {
    connection.createStatement().use { statement ->
        return statement.executeQuery(sql)
    }
}
