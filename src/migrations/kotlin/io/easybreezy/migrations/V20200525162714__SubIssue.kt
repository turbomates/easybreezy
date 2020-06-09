package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200525162714__SubIssue : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
                ALTER table issues ADD column parent UUID references issues(id) default null;
                """
                .trimIndent()
        )
    }
}
