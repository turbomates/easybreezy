package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200323152109__AddVacationDaysToLocations : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute("""
            ALTER TABLE locations ADD vacation_days int default null;
            UPDATE locations SET vacation_days = 25;
            ALTER TABLE locations ALTER COLUMN vacation_days SET NOT NULL;
            ALTER TABLE user_locations ADD extra_vacation_days int default null;
        """.trimIndent())
    }
}
