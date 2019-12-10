package io.easybreezy.user.application

import com.google.inject.Inject
import io.easybreezy.user.model.Email
import io.easybreezy.user.model.Password
import io.easybreezy.user.model.Repository
import io.easybreezy.user.model.User

class Handler @Inject constructor(private val repository: Repository) {

    fun handleInvite(command: InviteUser) {

        val user = User.invite(
            Email(command.email),
            Password(generatePassword()),
            mutableSetOf(User.Role.valueOf(command.role))
        )

        repository.addUser(user)
    }

    private fun generatePassword(): String {
        val alphabet = ('0'..'z').toList().toTypedArray()

        return (1..10).map { alphabet.random() }.joinToString("")
    }
}
