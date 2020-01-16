package io.easybreezy.user.application.queryobject

import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.user.model.Role
import io.easybreezy.user.model.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class UserQO(private val id: UUID) : QueryObject<User> {
    override fun getData() =
        transaction {
            Users.select {
                Users.id eq id
            }.first().toUser()
        }
}

//toDO сделать с пейджингом
class UsersQO : QueryObject<List<User>> {
    override fun getData() =
        transaction {
            Users.selectAll().map { it.toUser() }
        }
}

private fun ResultRow.toUser() = User(
    id = UUID.fromString(this[Users.id].toString()),
    email = this[Users.email.address],
    status = this[Users.status].toString(),
    roles = this[Users.roles],
    firstName = this[Users.name.firstName],
    lastName = this[Users.name.lastName]
)

data class User(
    val id: UUID,
    val email: String,
    val status: String,
    val roles: Set<Role>,
    val firstName: String?,
    val lastName: String?
)
