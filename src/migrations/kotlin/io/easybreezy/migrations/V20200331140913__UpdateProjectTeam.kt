package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200331140913__UpdateProjectTeam : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
                alter table project_teams add created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL;
                alter table project_teams add updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL;
            """.trimIndent()
        )
    }
}
