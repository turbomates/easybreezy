package io.easybreezy.user.application

import com.google.inject.Inject
import io.easybreezy.user.model.Role
import io.easybreezy.user.model.User
import io.easybreezy.user.model.Email
import io.easybreezy.user.model.Repository

class Handler @Inject constructor(private val repository: Repository) {

    fun handleInvite(command: Invite) {
        // val user = User.invite(
        //     Email(command.email),
        //     mutableSetOf(Role.valueOf(command.role))
        // )

        // repository.addUser(user)
    }

    // fun handleConfirm(command: Confirm) {
    //     val user = repository.findByToken(command.token)
    //     user.ifPresent {
    //         it.confirm(Password(command.password), Name(command.firstName, command.lastName))
    //     }
    // }
}
