package io.easybreezy.user.model

interface Repository {
    fun findByToken(token: String): User
}
