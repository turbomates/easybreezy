package io.easybreezy.user.model

import java.util.UUID

interface Repository {
    suspend fun getOne(id: UUID): User
    suspend fun find(id: UUID): User?
    suspend fun findByToken(token: String): User?
    suspend fun getByToken(token: String): User
    fun findByEmail(email: Email): User?
    fun findByUsername(username: String): User?
}
