package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200525161403__AddRepeatableEvent : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            alter table events add column is_repeatable bool default null;
            update events set is_repeatable = false;
            alter table events alter column is_repeatable set not null;
        """.trimIndent()
        )
    }
}
