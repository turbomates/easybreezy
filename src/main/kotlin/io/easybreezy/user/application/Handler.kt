package io.easybreezy.user.application

import com.google.inject.Inject
import io.easybreezy.user.model.Email
import io.easybreezy.user.model.Password
import io.easybreezy.user.model.Repository

class Handler @Inject constructor(private val repository: Repository) {

    fun handleInvite(command: Invite) {
       User.invite(
           Email.create(command.email),
           mutableSetOf(Role.valueOf(command.role))
       )
    }

    fun handleConfirm(command: Confirm) {
        val user = repository.getByToken(command.token)

       user.confirm(Password.create(command.password), command.firstName, command.lastName)
    }
}
