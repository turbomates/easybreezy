package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200120192527__CreateProjects : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            create table projects(
                id UUID not null primary key,
                author UUID not null,
                status VARCHAR(255) NOT NULL,
                slug VARCHAR(25) NOT NULL,
                name VARCHAR(255) NOT NULL,
                description TEXT DEFAULT NULL,
                created_at timestamp without time zone not null default timezone('UTC', statement_timestamp()),
                updated_at timestamp without time zone not null default timezone('UTC', statement_timestamp())
            );
            create unique index projects_slug_idx on projects(slug);
            create index projects_author_idx on projects(author);
        """.trimIndent()
        )
    }
}
