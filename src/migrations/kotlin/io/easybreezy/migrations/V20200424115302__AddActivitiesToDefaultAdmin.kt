package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200424115302__AddActivitiesToDefaultAdmin : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            update users SET activities = '[
               "USERS_LIST",
               "USERS_MANAGE",
               "ABSENCES_SELF_MANAGE",
               "ABSENCES_MANAGE",
               "ABSENCES_LIST",
               "LOCATIONS_MANAGE",
               "LOCATIONS_LIST",
               "USER_LOCATIONS_SELF_MANAGE",
               "USER_LOCATIONS_MANAGE",
               "USER_LOCATIONS_LIST",
               "EMPLOYEES_LIST",
               "EMPLOYEES_SELF_MANAGE",
               "EMPLOYEES_MANAGE",
               "CALENDARS_MANAGE",
               "CALENDARS_LIST",
               "HOLIDAYS_MANAGE",
               "VACATIONS_SHOW_MY",
               "VACATIONS_SHOW_ANY",
               "PROJECTS_CREATE",
               "PROJECTS_MANAGE",
               "PROJECTS_ROLES_MANAGE",
               "PROJECTS_CATEGORIES_MANAGE",
               "PROJECTS_SHOW_MY",
               "PROJECTS_SHOW_ANY",
               "TEAMS_CREATE",
               "TEAMS_ADD_MEMBERS",
               "TEAMS_MANAGE",
               "TEAMS_SHOW_MY",
               "TEAMS_SHOW_ANY",
               "TEAMS_MEMBERS_MANAGE"
            ]' 
            where email_address = 'admin@admin.my';
        """.trimIndent()
        )
    }
}
