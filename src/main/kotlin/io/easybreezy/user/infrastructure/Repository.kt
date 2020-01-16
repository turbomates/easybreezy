package io.easybreezy.user.infrastructure

import io.easybreezy.user.model.User
import io.easybreezy.user.model.Users
import io.easybreezy.user.model.exception.InvalidTokenException
import org.jetbrains.exposed.sql.transactions.transaction
import io.easybreezy.user.model.Repository as RepositoryInterface

class Repository : User.Repository(), RepositoryInterface {
    override fun findByToken(token: String): User? {
        return transaction {
            find { Users.token eq token }.firstOrNull()
        }
    }

    override fun getByToken(token: String): User {
        return findByToken(token) ?: throw InvalidTokenException(token)
    }

    override fun findByEmail(email: User.Email): User? {
        return transaction {
            find { Users.email.address eq email.address() }.firstOrNull()
        }
    }
}
