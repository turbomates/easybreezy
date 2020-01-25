package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200122125024__AddAbsenceReasonType : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            create type absence_reason as enum('VACATION', 'SICK', 'DAYON', 'PERSONAL');
        """.trimIndent()
        )
    }
}
