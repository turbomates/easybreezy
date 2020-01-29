package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200128143208__CreateLocations : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute("""
            create table locations(
                id uuid not null primary key,
                name text default null
            );
            create unique index locations_name_uniq_idx on locations(name);
        """.trimIndent())
    }
}
