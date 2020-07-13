package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200629131718__IssueNumber : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute("ALTER TABLE issues ADD COLUMN number INTEGER DEFAULT NULL")
        context.execute("ALTER TABLE issues ADD CONSTRAINT issues_project_number UNIQUE (number, project)")
    }
}
