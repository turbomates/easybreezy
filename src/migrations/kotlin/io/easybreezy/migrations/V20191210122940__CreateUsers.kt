package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20191210122940__CreateUsers : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            create type user_status as enum('ACTIVE', 'WAIT_CONFIRM');
        """.trimIndent()
        )

        context.execute(
            """
            create table users (
                id UUID NOT NULL PRIMARY KEY,
                password VARCHAR(255) DEFAULT NULL,
                email_address VARCHAR(255) NOT NULL,
                first_name VARCHAR(255) DEFAULT NULL,
                last_name VARCHAR(255) DEFAULT NULL,
                roles JSONB NOT NULL DEFAULT '[]'::jsonb,
                status user_status not null default 'ACTIVE',
                token VARCHAR(255) DEFAULT NULL
            )
        """.trimIndent()
        )
    }
}
