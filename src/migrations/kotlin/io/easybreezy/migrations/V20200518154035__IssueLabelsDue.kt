package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200518154035__IssueLabelsDue : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
                ALTER table issues ADD column start_date TIMESTAMP WITHOUT TIME ZONE default null;
                ALTER table issues ADD column due_date TIMESTAMP WITHOUT TIME ZONE default null;
            """.trimIndent()
        )

        context.execute(
            """
            create table issue_labels (
                id UUID NOT NULL PRIMARY KEY,
                name VARCHAR(25) NOT NULL,
                UNIQUE(name)
            )
            """.trimIndent()
        )

        context.execute(
            """
            create table issue_label (
                issue UUID REFERENCES issues (id),
                label UUID REFERENCES issue_labels (id),
                CONSTRAINT issue_label_pkey PRIMARY KEY (issue, label)
            )
            """.trimIndent()
        )
    }
}
