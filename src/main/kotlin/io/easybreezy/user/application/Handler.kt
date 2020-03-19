package io.easybreezy.user.application

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.infrastructure.ktor.auth.Role
import io.easybreezy.user.model.Email
import io.easybreezy.user.model.Password
import io.easybreezy.user.model.Repository
import io.easybreezy.user.model.User
import java.util.UUID

class Handler @Inject constructor(
    private val repository: Repository,
    private val transaction: TransactionManager
) {

    suspend fun handleInvite(command: Invite) {
        transaction {
            User.invite(
                Email.create(command.email),
                mutableSetOf(Role.valueOf(command.role))
            )
        }
    }

    suspend fun handleConfirm(command: Confirm) {
        transaction {
            val user = repository.getByToken(command.token)

            user.confirm(Password.create(command.password), command.firstName, command.lastName)
        }
    }

    suspend fun handleUpdateContacts(command: UpdateContacts, userId: UUID) {
        transaction {
            repository.getOne(userId).replaceContacts(command.contacts)
        }
    }
}
