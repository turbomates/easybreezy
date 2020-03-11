package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200224451178__CreateHolidays : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            create table holidays (
                id UUID NOT NULL PRIMARY KEY,
                day DATE NOT NULL,
                name TEXT NOT NULL,
                is_working_day BOOLEAN NOT NULL,
                calendar UUID NOT NULL REFERENCES calendars(id) ON DELETE CASCADE
            )
        """.trimIndent()
        )
        context.execute(
            """
            create unique index holidays_day_calendar_uniq_idx on holidays(day, calendar);
        """.trimIndent()
        )
    }
}
