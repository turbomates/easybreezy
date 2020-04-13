package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200413092747__AddAbsencesApprovement : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
                alter table absences add is_approved boolean not null default false;
            """.trimIndent()
        )
    }
}
