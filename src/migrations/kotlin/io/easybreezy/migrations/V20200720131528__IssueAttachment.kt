package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200720131528__IssueAttachment : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
                alter table issues drop column files;
            """.trimIndent()
        )
        context.execute(
            """
                CREATE TABLE issue_attachments (
                    issue UUID REFERENCES issues(id) NOT NULL PRIMARY KEY,                    
                    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
                );
            """.trimIndent()
        )

        context.execute(
            """
                CREATE TABLE files (
                    id UUID NOT NULL PRIMARY KEY,
                    title VARCHAR(255) NOT NULL,
                    extension VARCHAR(25) NOT NULL,
                    path VARCHAR(255) NOT NULL,
                    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
                );
            """.trimIndent()
        )

        context.execute(
            """
            create table issue_attachment_files (
                attachment UUID REFERENCES issue_attachments (issue),
                file UUID REFERENCES files (id),
                CONSTRAINT issue_attachment_file_pkey PRIMARY KEY (attachment, file)
            )
            """.trimIndent()
        )
    }
}
