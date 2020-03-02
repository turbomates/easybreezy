package io.easybreezy.user.application.queryobject

import io.easybreezy.infrastructure.exposed.toUUID
import io.easybreezy.infrastructure.ktor.auth.Role
import io.easybreezy.infrastructure.query.ContinuousList
import io.easybreezy.infrastructure.query.PagingParameters
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.query.toContinuousList
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import io.easybreezy.user.model.Users
import kotlinx.serialization.Serializable

class UserQO(private val userId: UUID) : QueryObject<User> {
    override suspend fun getData(): User {
        return transaction {
            Users.select {
                Users.id eq userId
            }.first().toUser()
        }
    }
}

class UsersQO(private val paging: PagingParameters) : QueryObject<ContinuousList<User>> {
    override suspend fun getData() =
        transaction {
            Users
                .selectAll()
                .limit(paging.pageSize, paging.offset)
                .map { it.toUser() }
                .toContinuousList(paging.pageSize, paging.currentPage)
        }
}

private fun ResultRow.toUser() = User(
    id = this[Users.id].toUUID(),
    email = this[Users.email].address,
    status = this[Users.status].toString(),
    roles = this[Users.roles]
)

@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val email: String,
    val status: String,
    val roles: Set<Role>
)
