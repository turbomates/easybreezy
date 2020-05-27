package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200513144119__CreateEvents : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
            create type event_status as enum('OPENED', 'CANCELLED');
        """.trimIndent()
        )

        context.execute(
            """
            create table events(
                id uuid not null primary key,
                name varchar(255) not null,
                status event_status not null default 'OPENED',
                description text default null,
                started_at timestamp without time zone not null,
                ended_at timestamp without time zone default null,
                is_private bool not null,
                is_free_entry bool default false,
                location uuid default null,
                owner uuid not null,
                created_at timestamp without time zone not null,
                updated_at timestamp without time zone default null
                
            );
        """.trimIndent()
        )

        context.execute(
            """
            create type event_participant_visit_status as enum('WAIT_RESPONSE', 'WILL_COME', 'MAYBE_COME', 'NOT_COME');
        """.trimIndent()
        )

        context.execute(
            """
            create table event_participants(
                id uuid not null primary key,
                visit_status event_participant_visit_status not null default 'WAIT_RESPONSE',
                event uuid not null,
                employee uuid not null
            );
            
            create unique index event_employee_participant_idx on event_participants(event, employee);
        """.trimIndent()
        )
    }
}
