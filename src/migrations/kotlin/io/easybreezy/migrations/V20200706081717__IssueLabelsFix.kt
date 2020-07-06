package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200706081717__IssueLabelsFix : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute("alter table issue_labels rename to labels")
        context.execute("alter table issue_label rename to issue_labels")
    }
}
