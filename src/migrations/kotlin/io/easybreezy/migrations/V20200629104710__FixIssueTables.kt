package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200629104710__FixIssueTables : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute("ALTER TABLE issue_estimates RENAME TO issue_timings")
        context.execute("ALTER TABLE issue_behavior RENAME TO issue_workflows")
    }
}
