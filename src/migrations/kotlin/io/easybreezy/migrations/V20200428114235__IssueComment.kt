package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200428114235__IssueComment : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
                create table issue_comments (
                    id UUID NOT NULL PRIMARY KEY,                   
                    issue_id UUID REFERENCES issues(id),
                    author UUID NOT NULL,
                    comment VARCHAR(500) NOT NULL,
                    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
                )
            """.trimIndent()
        )

        context.execute(
            """
                alter table issues add watchers JSONB NOT NULL DEFAULT '[]'::jsonb
            """.trimIndent()
        )

        context.execute(
            """
                alter table issues add priority smallint NOT NULL DEFAULT 0
            """.trimIndent()
        )

        context.execute(
            """
                alter table issues add author UUID REFERENCES users(id)
            """.trimIndent()
        )

        context.execute(
            """
                alter table issues add description TEXT DEFAULT NULL
            """.trimIndent()
        )

        context.execute(
            """
                alter table issues add project UUID REFERENCES projects(id)
            """.trimIndent()
        )

        context.execute(
            """
                alter table issues add title VARCHAR DEFAULT NULL
            """.trimIndent()
        )

        context.execute(
            """
                alter table issues alter column category DROP NOT NULL
            """.trimIndent()
        )

        context.execute(
            """
                alter table issues DROP COLUMN name
            """.trimIndent()
        )

        context.execute(
            """
                alter table issues ADD column assignee UUID REFERENCES users(id) DEFAULT NULL
            """.trimIndent()
        )
    }
}
