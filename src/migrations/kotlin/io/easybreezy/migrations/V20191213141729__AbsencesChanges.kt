package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20191213141729__AbsencesChanges : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute("""
            alter table working_hours drop absenceid;
            alter table working_hours add user_id uuid not null
        """.trimIndent())
    }
}
