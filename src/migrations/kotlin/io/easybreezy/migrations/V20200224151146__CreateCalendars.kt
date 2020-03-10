package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200224151146__CreateCalendars : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            create table calendars (
                id UUID NOT NULL PRIMARY KEY,
                name TEXT NOT NULL,
                location UUID NOT NULL
            )
        """.trimIndent()
        )
        context.execute(
            """
            create unique index calendars_location_uniq_idx on calendars(location);
        """.trimIndent()
        )
    }
}
