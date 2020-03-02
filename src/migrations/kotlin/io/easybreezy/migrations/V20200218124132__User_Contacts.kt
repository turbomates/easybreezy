package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200218124132__User_Contacts : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            create type contact_type as enum('PHONE', 'EMAIL', 'SKYPE', 'TELEGRAM', 'SLACK');
        """.trimIndent()
        )

        context.execute(
            """
            create table contacts (
                id UUID NOT NULL PRIMARY KEY,
                value VARCHAR(100) NOT NULL,
                type contact_type NOT NULL,
                user_id UUID REFERENCES users(id)
            )
        """.trimIndent()
        )

        context.execute(
            """
                ALTER TABLE users ADD column first_name varchar(100) default null;
                ALTER TABLE users ADD column last_name varchar(100) default null;
                """
        )

        context.execute(
            """
                ALTER TABLE employees DROP column first_name;
                ALTER TABLE employees DROP column last_name;
                DROP table messengers;
                DROP type messenger_type;
                """
        )
    }
}
