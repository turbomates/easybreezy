package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200615081548__IssueDecomposition : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute("DROP TABLE issue_solutions")

        context.execute(
            """
                CREATE TABLE issue_behavior (
                    issue UUID REFERENCES issues(id) NOT NULL,
                    status UUID REFERENCES issue_statuses(id) DEFAULT NULL,
                    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
                );
            """.trimIndent()
        )
        context.execute(
            """
                CREATE TABLE issue_participants (
                    issue UUID REFERENCES issues(id) NOT NULL,
                    watchers JSONB NOT NULL DEFAULT '[]'::jsonb,
                    assignee UUID REFERENCES users(id) DEFAULT NULL,
                    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
                );
            """.trimIndent()
        )
        context.execute(
            """
                CREATE TABLE issue_estimates (
                    issue UUID REFERENCES issues(id) NOT NULL,
                    due_date TIMESTAMP WITHOUT TIME ZONE DEFAULT NULL,
                    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
                );
            """.trimIndent()
        )
    }
}
