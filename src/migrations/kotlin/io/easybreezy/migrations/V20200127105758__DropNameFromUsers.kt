package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200127105758__DropNameFromUsers : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute("""
            ALTER TABLE users drop first_name;
            ALTER TABLE users drop last_name;
        """.trimIndent())
    }
}
