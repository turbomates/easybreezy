package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200608151725__IssueSolution : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
                ALTER TABLE issues 
                    DROP COLUMN status CASCADE,
                    DROP COLUMN watchers,
                    DROP COLUMN assignee CASCADE,
                    DROP COLUMN start_date,
                    DROP COLUMN due_date
                """
                .trimIndent()
        )

        context.execute(
            """
                CREATE TABLE issue_solutions (
                    issue UUID REFERENCES issues(id) NOT NULL,
                    status UUID REFERENCES issue_statuses(id) DEFAULT NULL,
                    watchers JSONB NOT NULL DEFAULT '[]'::jsonb,
                    assignee UUID REFERENCES users(id) DEFAULT NULL,
                    due_date TIMESTAMP WITHOUT TIME ZONE DEFAULT NULL,
                    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
                );
            """.trimIndent()
        )
    }
}
