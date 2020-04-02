package io.easybreezy.hr.application.hr.queryobject

import io.easybreezy.hr.model.hr.Employees
import io.easybreezy.infrastructure.query.ContinuousList
import io.easybreezy.infrastructure.query.PagingParameters
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.query.toContinuousList
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import io.easybreezy.user.model.NameTable
import io.easybreezy.user.model.Users
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class EmployeesQO(private val paging: PagingParameters) : QueryObject<ContinuousList<Employee>> {
    override suspend fun getData(): ContinuousList<Employee> {
        return transaction {
                Employees.innerJoin(Users, { Employees.userId }, { Users.id })
                .selectAll()
                .andWhere { Employees.fired eq false }
                .limit(paging.pageSize, paging.offset)
                .map { it.toEmployee() }
                .toContinuousList(paging.pageSize, paging.currentPage)
        }
    }
}

internal fun ResultRow.toEmployee() = Employee(
    this[Employees.userId],
    this[Users.name[NameTable.firstName]],
    this[Users.name[NameTable.lastName]]
)

@Serializable
data class Employee(
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    val firstName: String?,
    val lastName: String?
)
