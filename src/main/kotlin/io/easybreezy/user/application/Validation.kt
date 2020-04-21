package io.easybreezy.user.application

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.infrastructure.ktor.validate
import io.easybreezy.user.model.Contacts
import io.easybreezy.user.model.Email
import io.easybreezy.user.model.Repository
import io.easybreezy.user.model.User
import org.valiktor.Constraint
import org.valiktor.Validator
import org.valiktor.functions.isEmail
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.functions.validateForEach

class Validation @Inject constructor(
    private val transactionManager: TransactionManager,
    private val repository: Repository
) {
    object Unique : Constraint {
        override val name: String
            get() = "User with this email already exist"
    }

    private fun <E> Validator<E>.Property<String?>.isUnique(): Validator<E>.Property<String?> =
        this.validate(Unique) { value ->
            repository.findByEmail(Email.create(value!!)) !is User
        }

    suspend fun onCreate(command: Create): List<Error> {
        return transactionManager {
            validate(command) {
                validate(Create::email).isNotNull().isNotBlank().isUnique()
                validate(Create::firstName).isNotNull().isNotBlank()
                validate(Create::lastName).isNotNull().isNotBlank()
                validate(Create::role).isNotNull().isNotBlank()
            }
        }
    }

    suspend fun onInvite(command: Invite): List<Error> {
        return transactionManager {
            validate(command) {
                validate(Invite::email).isNotNull().isNotBlank().isUnique()
                validate(Invite::role).isNotNull().isNotBlank()
            }
        }
    }

    fun onConfirm(command: Confirm): List<Error> {
        return validate(command) {
            validate(Confirm::token).isNotNull().isNotBlank()
            validate(Confirm::password).isNotNull().isNotBlank()
            validate(Confirm::firstName).isNotNull().isNotBlank()
            validate(Confirm::lastName).isNotNull().isNotBlank()
        }
    }

    fun onUpdateContacts(command: UpdateContacts): List<Error> {
        return validate(command) {
            validate(UpdateContacts::contacts)
                .validateForEach { contact ->
                    validate(Contact::value).isNotBlank()
                    if (contact.type == Contacts.Type.EMAIL) {
                        validate(Contact::value).isEmail()
                    }
                }
        }
    }
}
