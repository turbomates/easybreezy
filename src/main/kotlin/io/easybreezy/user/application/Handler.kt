package io.easybreezy.user.application

import com.google.inject.Inject
import io.easybreezy.user.model.Repository
import io.easybreezy.user.model.Role
import io.easybreezy.user.model.User

class Handler @Inject constructor(private val repository: Repository) {

    fun handleInvite(command: Invite) {
        User.invite(
            User.Email(command.email),
            mutableSetOf(Role.valueOf(command.role))
        )
    }

    fun handleConfirm(command: Confirm) {
        val user = repository.findByToken(command.token)

        user.confirm(User.Password(command.password), User.Name(command.firstName, command.lastName))
    }
}
