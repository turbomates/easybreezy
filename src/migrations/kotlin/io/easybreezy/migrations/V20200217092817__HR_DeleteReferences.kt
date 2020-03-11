package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200217092817__HR_DeleteReferences : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
                ALTER TABLE employees DROP column current_salary_id CASCADE;
                ALTER TABLE employees DROP column current_position_id CASCADE;
                """
        )
    }
}
