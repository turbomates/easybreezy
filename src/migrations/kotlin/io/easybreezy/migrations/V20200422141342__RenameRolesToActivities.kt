package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200422141342__RenameRolesToActivities : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            alter table users rename column roles to activities;
        """.trimIndent()
        )
    }
}
