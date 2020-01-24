package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200123084246__CreateMessengers : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            create type messenger_type as enum('SKYPE', 'TELEGRAM', 'SLACK');
        """.trimIndent()
        )

        context.execute(
            """
            create table messengers (
                id UUID NOT NULL PRIMARY KEY,
                username VARCHAR(255) NOT NULL,
                type messenger_type NOT NULL,
                profile UUID NOT NULL
            )
        """.trimIndent()
        )
    }
}
