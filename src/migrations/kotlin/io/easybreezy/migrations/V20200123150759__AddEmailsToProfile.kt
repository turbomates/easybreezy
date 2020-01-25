package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200123150759__AddEmailsToProfile : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute("""
            ALTER TABLE profiles ADD emails JSONB NOT NULL DEFAULT '[]'::jsonb;
            ALTER TABLE profiles drop messengers;
        """.trimIndent())
    }
}
