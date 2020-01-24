package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200122140007__WorkingHoursAlterDay : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            alter table working_hours drop column day;
            alter table working_hours add column day date not null;
        """.trimIndent()
        )
    }
}
