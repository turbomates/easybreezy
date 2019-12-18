package io.easybreezy.user.model

interface Repository {
    fun addUser(user: User)
    // fun findUser(id: UserId): User
    // fun findByToken(token: String): Optional<User>
}
