package io.easybreezy.user.application

import com.google.inject.Inject
import io.easybreezy.user.model_legacy.Email
import io.easybreezy.user.model_legacy.Name
import io.easybreezy.user.model_legacy.Password
import io.easybreezy.user.model_legacy.Repository
import io.easybreezy.user.model_legacy.User

class Handler @Inject constructor(private val repository: Repository) {

    fun handleInvite(command: Invite) {
        val user = User.invite(
            Email(command.email),
            mutableSetOf(User.Role.valueOf(command.role))
        )

        repository.addUser(user)
    }

    fun handleConfirm(command: Confirm) {
        val user = repository.findByToken(command.token)
        user.ifPresent {
            it.confirm(Password(command.password), Name(command.firstName, command.lastName))
        }
    }
}
