package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20191212113813__CreateAbsences : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute("""
            create table absences(
                id uuid not null primary key,
                started_at timestamp without time zone not null default timezone('UTC', statement_timestamp()),
                ended_at timestamp without time zone not null default timezone('UTC', statement_timestamp()),
                comment text default null,
                reason varchar(20) not null,
                userId uuid not null
            )
        """.trimIndent())
    }
}
