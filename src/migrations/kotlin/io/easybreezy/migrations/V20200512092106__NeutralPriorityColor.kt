package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200512092106__NeutralPriorityColor : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
                ALTER TYPE priority_color ADD VALUE '#FFFFFF';
            """.trimIndent()
        )
    }
}
