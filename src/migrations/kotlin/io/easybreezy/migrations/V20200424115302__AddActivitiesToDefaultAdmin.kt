package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200424115302__AddActivitiesToDefaultAdmin : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            update users SET activities = '[
               "USERS_MANAGE",
               "ABSENCES_MANAGE",
               "ABSENCES_LIST",
               "LOCATIONS_MANAGE",
               "LOCATIONS_LIST",
               "USER_LOCATIONS_MANAGE",
               "USER_LOCATIONS_LIST",
               "EMPLOYEES_LIST",
               "EMPLOYEES_MANAGE",
               "CALENDARS_MANAGE",
               "CALENDARS_LIST",
               "HOLIDAYS_MANAGE",
               "VACATIONS_SHOW",
               "PROJECTS_MANAGE",
               "PROJECTS_SHOW",
            ]' 
            where email_address = 'admin@admin.my';
        """.trimIndent()
        )
    }
}
