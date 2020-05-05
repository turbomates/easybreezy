package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200420095401__IssueStatuses : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            CREATE TABLE issue_statuses(
                id UUID NOT NULL PRIMARY KEY,
                project UUID REFERENCES projects(id) NOT NULL,
                name VARCHAR(25) NOT NULL,
                UNIQUE(name, project)
            )
        """.trimIndent()
        )

        context.execute(
            """
                ALTER TABLE issues ADD status UUID REFERENCES issue_statuses(id) DEFAULT NULL
            """.trimIndent()
        )
    }
}
