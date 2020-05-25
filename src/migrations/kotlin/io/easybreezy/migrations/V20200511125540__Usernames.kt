package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200511125540__Usernames : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            alter table users add username VARCHAR(25) DEFAULT NULL;
            update users set username=lower(concat_ws('_', first_name, last_name));
        """.trimIndent()
        )
    }
}
