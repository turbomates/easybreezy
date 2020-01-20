package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200110130509__CreateProfile : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            create type profile_gender as enum('MALE', 'FEMALE');
        """.trimIndent()
        )

        context.execute(
            """
            create table profiles (
                id UUID NOT NULL PRIMARY KEY,
                user_id UUID NOT NULL,
                birthday TIMESTAMP WITHOUT TIME ZONE DEFAULT NULL,
                about TEXT DEFAULT NULL,
                gender profile_gender DEFAULT NULL,
                messengers JSONB NOT NULL DEFAULT '[]'::jsonb,
                phones JSONB NOT NULL DEFAULT '[]'::jsonb
            )
        """.trimIndent()
        )
    }
}
