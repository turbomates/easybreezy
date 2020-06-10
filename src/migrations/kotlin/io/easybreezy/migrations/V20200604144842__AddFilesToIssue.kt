package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200604144842__AddFilesToIssue : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            alter table issues add column files JSONB NOT NULL DEFAULT '[]'::jsonb;
        """.trimIndent()
        )
    }
}
