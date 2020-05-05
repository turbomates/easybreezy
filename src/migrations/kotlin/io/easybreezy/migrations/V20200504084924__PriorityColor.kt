package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200504084924__PriorityColor : BaseJavaMigration() {
    override fun migrate(context: Context) {

        context.execute(
            """
                create type priority_color as enum('#FF0000', '#00FF00', '#FFFF00');
            """.trimIndent()
        )

        context.execute(
            """
                alter table issues ADD column color priority_color DEFAULT NULL
            """.trimIndent()
        )
    }
}
