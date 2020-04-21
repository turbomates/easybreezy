package io.easybreezy.user.application.queryobject

import io.easybreezy.infrastructure.query.ContinuousList
import io.easybreezy.infrastructure.query.PagingParameters
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.query.toContinuousList
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import io.easybreezy.user.model.EmailTable
import io.easybreezy.user.model.Users
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

class UserQO(private val userId: UUID) : QueryObject<User> {
    override suspend fun getData() =
        Users
            .select {
                Users.id eq userId
            }.first().toUser()
}

class UsersQO(private val paging: PagingParameters) : QueryObject<ContinuousList<User>> {
    override suspend fun getData() =
        Users
            .selectAll()
            .toContinuousList(paging, ResultRow::toUser)
}

private fun ResultRow.toUser(): User {
    return User(
        id = this[Users.id].value,
        email = this[Users.email[EmailTable.email]],
        status = this[Users.status].toString(),
        comment = this[Users.comment],
        roles = this[Users.roles]
    )
}

@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val email: String?,
    val status: String,
    val comment: String?,
    val roles: Set<String>
)
