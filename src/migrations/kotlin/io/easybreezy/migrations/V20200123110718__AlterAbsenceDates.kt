package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200123110718__AlterAbsenceDates : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            alter table absences drop column started_at;
            alter table absences add column started_at date not null;
            alter table absences drop column ended_at;
            alter table absences add column ended_at date not null;
        """.trimIndent()
        )
    }
}
