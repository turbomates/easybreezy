package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200212133804__HR : BaseJavaMigration() {
    override fun migrate(context: Context) {

        context.execute(
            """
            create table employees (
                id UUID NOT NULL PRIMARY KEY,
                user_id UUID NOT NULL,
                first_name VARCHAR(100) NOT NULL,
                last_name VARCHAR(100) NOT NULL,
                birthday DATE DEFAULT NULL,
                bio TEXT DEFAULT NULL,
                skills JSONB NOT NULL DEFAULT '[]'::jsonb,
                fired BOOLEAN NOT NULL DEFAULT FALSE,
                created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
            )
        """.trimIndent()
        )

        context.execute(
            """
                create table employee_notes (
                    id UUID NOT NULL PRIMARY KEY,
                    hr_manager_id UUID NOT NULL,
                    employee_id UUID REFERENCES employees(id),
                    text TEXT NOT NULL,
                    archived BOOLEAN NOT NULL DEFAULT FALSE,
                    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
                )
            """.trimIndent()
        )

        context.execute(
            """
                create table employee_salaries (
                    id UUID NOT NULL PRIMARY KEY,                   
                    employee_id UUID REFERENCES employees(id),
                    amount BIGINT NOT NULL,
                    since DATE NOT NULL,
                    till DATE DEFAULT NULL,
                    hr_manager_id UUID NOT NULL,
                    comment VARCHAR(500) NOT NULL,
                    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
                )
            """.trimIndent()
        )

        context.execute(
            """
                create table employee_positions (
                    id UUID NOT NULL PRIMARY KEY,                   
                    employee_id UUID REFERENCES employees(id),
                    title VARCHAR(100) NOT NULL,
                    since DATE NOT NULL,
                    till DATE DEFAULT NULL,
                    hr_manager_id UUID NOT NULL,                    
                    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
                )
            """.trimIndent()
        )

        context.execute(
            """
                ALTER TABLE employees ADD current_position_id UUID REFERENCES employee_positions(id) DEFAULT NULL; 
                ALTER TABLE employees ADD current_salary_id UUID REFERENCES employee_salaries(id) DEFAULT NULL; 
            """.trimIndent()
        )
    }
}
