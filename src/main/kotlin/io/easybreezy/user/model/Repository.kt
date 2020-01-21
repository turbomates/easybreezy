package io.easybreezy.user.model

import java.util.UUID

interface Repository {
    fun getOne(id: UUID): User
    fun find(id: UUID): User?
    fun findByToken(token: String): User?
    fun getByToken(token: String): User
    fun findByEmail(email: User.Email): User?
}
