package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200122130827__RenameAbsencesUserId : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            alter table absences rename userid to user_id;
        """.trimIndent()
        )
    }
}
