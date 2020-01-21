package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200120195714__CreateProjectTeams : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            create table project_teams(
                id UUID not null primary key,
                project UUID not null,
                name VARCHAR(255) NOT NULL,
                status VARCHAR(255) NOT NULL
            );
                create index projects_teams_project_idx on project_teams(project);
        """.trimIndent()
        )
    }
}
