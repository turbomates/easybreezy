package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200129150515__FixUniquieIndexesWithUser : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            drop index absences_started_at_uniq_idx;
            drop index absences_ended_at_uniq_idx;
            drop index working_hours_day_uniq_idx;
            drop index user_locations_started_at_uniq_idx;
            drop index user_locations_ended_at_uniq_idx;
            create unique index absences_started_at_uniq_idx on absences(started_at, user_id);
            create unique index absences_ended_at_uniq_idx on absences(ended_at, user_id);
            create unique index working_hours_day_uniq_idx on working_hours(day, user_id);
            create unique index user_locations_started_at_uniq_idx on user_locations(started_at, user_id);
            create unique index user_locations_ended_at_uniq_idx on user_locations(ended_at, user_id);
        """.trimIndent()
        )
    }
}
