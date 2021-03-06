package io.easybreezy.hr.infrastructure

import io.easybreezy.hr.model.hr.Employee
import io.easybreezy.hr.model.hr.Employees
import io.easybreezy.hr.model.hr.Repository
import java.util.UUID

class EmployeeRepository : Employee.Repository(), Repository {
    override fun getByUserId(userId: UUID): Employee {
        return find { Employees.id eq userId }.first()
    }
}
