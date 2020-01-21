package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200120195659__CreateProjectRoles : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            create table project_roles(
                id UUID not null primary key,
                project UUID not null,
                name VARCHAR(255) NOT NULL,
                permissions JSONB NOT NULL DEFAULT '[]'::jsonb
            );
            create index projects_roles_project_idx on project_roles(project);
        """.trimIndent()
        )
    }
}
