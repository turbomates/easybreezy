package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20191210134804__CreateDomainEvents : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute("""
            create table domain_events(
                event JSONB NOT NULL,
                id UUID not null primary key,
                created_at timestamp without time zone not null default timezone('UTC', statement_timestamp()),
                published_at timestamp without time zone default NULL
            )
        """.trimIndent())
    }
}
