package io.easybreezy.user.application

import com.google.inject.Inject
import io.easybreezy.user.model.Email
import io.easybreezy.user.model.Password
import io.easybreezy.user.model.Repository
import io.easybreezy.user.model.Role
import io.easybreezy.user.model.User

class Handler @Inject constructor(private val repository: Repository) {

    fun handleInvite(command: Invite) {
        User.invite(
            Email(command.email),
            mutableSetOf(Role.valueOf(command.role))
        )
    }

    fun handleConfirm(command: Confirm) {
        val user = repository.findByToken(command.token)

        val test = "asd"

        user.confirm(Password(command.password), User.Name(command.firstName, command.lastName))
    }
}
