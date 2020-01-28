package io.easybreezy.user.infrastructure

import io.easybreezy.user.model.Email
import io.easybreezy.user.model.User
import io.easybreezy.user.model.Users
import io.easybreezy.user.model.exception.InvalidTokenException
import org.jetbrains.exposed.sql.transactions.transaction
import io.easybreezy.user.model.Repository
import io.easybreezy.user.model.exception.UserNotFoundException
import java.util.UUID

class UserRepository : User.Repository(), Repository {

    override fun getOne(id: UUID): User {
        return find(id) ?: throw UserNotFoundException(id)
    }

    override fun find(id: UUID): User? {
        return transaction {
            find { Users.id eq id }.firstOrNull()
        }
    }

    override fun findByToken(token: String): User? {
        return transaction {
            find { Users.token eq token }.firstOrNull()
        }
    }

    override fun getByToken(token: String): User {
        return findByToken(token) ?: throw InvalidTokenException(token)
    }

    override fun findByEmail(email: Email): User? {
        return find { Users.email eq email.address }.firstOrNull()
    }
}
