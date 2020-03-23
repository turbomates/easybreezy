package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200323092053__ProjectMember : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
                create table project_members (
                    id UUID NOT NULL PRIMARY KEY,
                    user_id UUID NOT NULL,
                    role UUID REFERENCES project_roles(id)
                )
            """.trimIndent()
        )
    }
}
