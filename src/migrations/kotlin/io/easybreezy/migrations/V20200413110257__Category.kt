package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200413110257__Category : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            CREATE TABLE issue_categories(
                id UUID NOT NULL PRIMARY KEY,
                project UUID REFERENCES projects(id) NOT NULL,
                name VARCHAR(25) NOT NULL,
                parent UUID REFERENCES issue_categories(id) DEFAULT NULL,
                UNIQUE(name, project)
            )
        """.trimIndent()
        )

        context.execute(
            """
            CREATE TABLE issues(
                id UUID NOT NULL PRIMARY KEY,
                category UUID REFERENCES issue_categories(id) DEFAULT NULL,
                name VARCHAR(100) NOT NULL,
                created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
            )
        """.trimIndent()
        )
    }
}
