package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200129095834__CreateUserLocations : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            create table user_locations (
                id UUID NOT NULL PRIMARY KEY,
                started_at DATE NOT NULL,
                ended_at DATE NOT NULL,
                location UUID NOT NULL,
                user_id UUID NOT NULL
            )
        """.trimIndent()
        )
        context.execute(
            """
            create unique index user_locations_started_at_uniq_idx on user_locations(started_at);
            create unique index user_locations_ended_at_uniq_idx on user_locations(ended_at);
        """.trimIndent()
        )
    }
}
