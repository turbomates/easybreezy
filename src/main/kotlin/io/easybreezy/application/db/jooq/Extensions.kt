package io.easybreezy.application.db.jooq

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import javax.sql.DataSource

fun <TReturn> DataSource.jooqDSL(block: (DSLContext) -> TReturn): TReturn {
    return this.connection.use { connection ->
        DSL.using(connection, SQLDialect.POSTGRES_10).use { dslContext ->
            block(dslContext)
        }
    }
}
