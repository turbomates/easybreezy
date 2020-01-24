package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200123142600__CreateAbsencesUniqueIndexes : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            create unique index absences_started_at_uniq_idx on absences(started_at);
            create unique index absences_ended_at_uniq_idx on absences(ended_at);
            create unique index working_hours_day_uniq_idx on working_hours(day);
        """.trimIndent()
        )
    }
}
