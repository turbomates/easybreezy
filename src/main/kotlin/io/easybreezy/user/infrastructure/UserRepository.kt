package io.easybreezy.user.infrastructure

import io.easybreezy.user.model.Email
import io.easybreezy.user.model.EmailTable
import io.easybreezy.user.model.Repository
import io.easybreezy.user.model.User
import io.easybreezy.user.model.Users
import io.easybreezy.user.model.exception.InvalidTokenException
import io.easybreezy.user.model.exception.UserNotFoundException
import org.jetbrains.exposed.sql.lowerCase
import java.util.UUID

class UserRepository : User.Repository(), Repository {

    override suspend fun getOne(id: UUID): User {
        return find(id) ?: throw UserNotFoundException(id)
    }

    override suspend fun find(id: UUID): User? {
        return find { Users.id eq id }.firstOrNull()
    }

    override suspend fun findByToken(token: String): User? {
        return find { Users.token eq token }.firstOrNull()
    }

    override suspend fun getByToken(token: String): User {
        return findByToken(token) ?: throw InvalidTokenException(token)
    }

    override fun findByEmail(email: Email): User? {
        return find { Users.email[EmailTable.email].lowerCase() eq email.address.toLowerCase() }.firstOrNull()
    }

    override fun findByUsername(username: String): User? {
        return find { Users.username.lowerCase() eq username.toLowerCase() }.firstOrNull()
    }
}
