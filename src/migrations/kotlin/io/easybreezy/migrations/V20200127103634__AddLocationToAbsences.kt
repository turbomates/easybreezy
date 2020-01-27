package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200127103634__AddLocationToAbsences : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            alter table absences add column location text default null;
        """.trimIndent()
        )
    }
}
