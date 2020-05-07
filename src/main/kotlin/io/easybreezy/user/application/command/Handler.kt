package io.easybreezy.user.application.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.user.model.Email
import io.easybreezy.user.model.Password
import io.easybreezy.user.model.Repository
import io.easybreezy.user.model.User
import java.util.UUID

class Handler @Inject constructor(private val repository: Repository, private val transaction: TransactionManager) {

    suspend fun handleCreate(command: Create) {
        transaction {
            User.create(
                Email.create(command.email),
                User.Name.create(command.firstName, command.lastName),
                command.activities
            )
        }
    }

    suspend fun handleInvite(command: Invite) {
        transaction {
            User.invite(
                Email.create(command.email),
                command.activities
            )
        }
    }

    suspend fun handleHire(userId: UUID) {
        transaction {
            val user = repository.getOne(userId)
            user.hire()
        }
    }

    suspend fun handleFire(userId: UUID) {
        transaction {
            val user = repository.getOne(userId)
            user.fire()
        }
    }

    suspend fun handleArchive(command: Archive) {
        transaction {
            val user = repository.getOne(command.userId)
            user.archive(command.reason)
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

    suspend fun handleUpdateActivities(command: UpdateActivities) {
        transaction {
            repository.getOne(command.userId).replaceActivities(command.activities)
        }
    }
}
