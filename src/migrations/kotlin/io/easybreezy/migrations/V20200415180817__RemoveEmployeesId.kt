package io.easybreezy.migrations

import io.easybreezy.migrations.extensions.execute
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V20200415180817__RemoveEmployeesId : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.execute(
            """
          ALTER TABLE employee_notes DROP CONSTRAINT employee_notes_employee_id_fkey;
          ALTER TABLE employee_salaries DROP CONSTRAINT employee_salaries_employee_id_fkey;
          ALTER TABLE employee_positions DROP CONSTRAINT employee_positions_employee_id_fkey;
          ALTER TABLE employees DROP CONSTRAINT employees_pkey;
          ALTER TABLE employees ADD PRIMARY KEY (user_id);
          UPDATE employee_positions SET employee_id= (Select user_id from employees where id=employee_id);
          UPDATE employee_salaries SET employee_id= (Select user_id from employees where id=employee_id);
          UPDATE employee_notes SET employee_id= (Select user_id from employees where id=employee_id);
          ALTER TABLE employee_positions RENAME COLUMN employee_id TO user_id;
          ALTER TABLE employee_salaries RENAME COLUMN employee_id TO user_id;
          ALTER TABLE employee_notes RENAME COLUMN employee_id TO user_id;
          ALTER TABLE employee_positions ADD CONSTRAINT employee_positions_employee_id_fkey FOREIGN KEY (user_id) REFERENCES employees (user_id); 
          ALTER TABLE employee_salaries ADD CONSTRAINT employee_salaries_employee_id_fkey FOREIGN KEY (user_id) REFERENCES employees (user_id); 
          ALTER TABLE employee_notes ADD CONSTRAINT employee_notes_employee_id_fkey FOREIGN KEY (user_id) REFERENCES employees (user_id); 
          ALTER TABLE employees drop column id;
        """.trimIndent()
        )
    }
}
