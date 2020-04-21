package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200420120448__ChangeUserStatuses : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            alter type user_status add value 'CREATED' before 'ACTIVE';
            
        """.trimIndent()
        )
    }

    override fun canExecuteInTransaction(): Boolean {
        return false
    }
}
