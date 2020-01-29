package io.easybreezy.user.application.queryobject

import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.user.model.Role
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class UserQO(private val userId: UUID) : QueryObject<User> {
    override fun getData() = User(UUID.randomUUID(), "", "", emptySet(), "", "")
       // transaction {
       //     Users.select {
       //         Users.id eq userId
       //     }.first()
       // }
}

// toDO сделать с пейджингом
class UsersQO : QueryObject<List<User>> {
    override fun getData() =
        transaction {
            emptyList<User>()
//            Users.selectAll().map { it.toUser() }
        }
}

// private fun ResultRow.toUser() = User(
//    id = this[Users.id].toUUID(),
//    email = this[Users.email.address],
//    status = this[Users.status].toString(),
//    roles = this[Users.roles]
//    firstName = this[Users.name.firstName],
//    lastName = this[Users.name.lastName]
// )

data class User(
    val id: UUID,
    val email: String,
    val status: String,
    val roles: Set<Role>,
    val firstName: String?,
    val lastName: String?
)
