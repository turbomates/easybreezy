package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200128125255__AddCreatedAtAndSetEmailUniqueInUsers : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute("""
            ALTER TABLE users ADD created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NULL;
            UPDATE users SET created_at = now();
            ALTER TABLE users ALTER COLUMN created_at SET NOT NULL;
            CREATE UNIQUE INDEX users_email_address_uniq_idx on users(email_address);
        """.trimIndent())
    }
}
