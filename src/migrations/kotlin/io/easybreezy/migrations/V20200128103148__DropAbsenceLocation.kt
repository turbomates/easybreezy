package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200128103148__DropAbsenceLocation : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            alter table absences drop column location;
        """.trimIndent()
        )
    }
}
