package io.easybreezy.hr.application.hr.queryobject

import io.easybreezy.hr.model.hr.Employees
import io.easybreezy.hr.model.location.UserLocations
import io.easybreezy.infrastructure.query.ContinuousList
import io.easybreezy.infrastructure.query.PagingParameters
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.query.toContinuousList
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import io.easybreezy.user.model.NameTable
import io.easybreezy.user.model.Users
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import java.util.UUID

class EmployeesQO(private val paging: PagingParameters) : QueryObject<ContinuousList<Employee>> {
    override suspend fun getData() =
        Employees.innerJoin(Users, { id }, { Users.id })
            .join(UserLocations, JoinType.LEFT, additionalConstraint = { UserLocations.userId eq Employees.id and UserLocations.endedAt.isNull() })
            .selectAll()
            .andWhere { Employees.fired eq false }
            .toContinuousList(paging, ResultRow::toEmployee)
}

internal fun ResultRow.toEmployee() = Employee(
    this[Employees.id].value,
    this[Users.name[NameTable.firstName]],
    this[Users.name[NameTable.lastName]],
    @Suppress("UNNECESSARY_SAFE_CALL")
    this[UserLocations.id]?.value
)

@Serializable
data class Employee(
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    val firstName: String?,
    val lastName: String?,
    @Serializable(with = UUIDSerializer::class)
    val currentLocationId: UUID?
)
