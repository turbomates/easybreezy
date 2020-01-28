package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200127133040__AddNameToProfile : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute("""
            ALTER TABLE profiles ADD first_name VARCHAR(255) DEFAULT NULL;
            ALTER TABLE profiles ADD last_name VARCHAR(255) DEFAULT NULL;
        """.trimIndent())
    }
}
