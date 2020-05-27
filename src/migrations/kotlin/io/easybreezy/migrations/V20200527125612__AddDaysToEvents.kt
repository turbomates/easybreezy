package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200527125612__AddDaysToEvents : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            alter table events add column days JSONB NOT NULL DEFAULT '[]'::jsonb;
        """.trimIndent()
        )
    }
}
