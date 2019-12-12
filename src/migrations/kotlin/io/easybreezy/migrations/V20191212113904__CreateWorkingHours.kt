package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20191212113904__CreateWorkingHours : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute("""
            create table working_hours(
                id uuid not null primary key,
                day timestamp without time zone not null default timezone('UTC', statement_timestamp()),
                count int not null,
                absenceId uuid not null
            )
        """.trimIndent())
    }
}
