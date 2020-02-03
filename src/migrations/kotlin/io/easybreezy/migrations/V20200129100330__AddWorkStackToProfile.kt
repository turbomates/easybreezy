package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200129100330__AddWorkStackToProfile : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute("""
            ALTER TABLE profiles ADD work_stack TEXT DEFAULT NULL;
        """.trimIndent())
    }
}
