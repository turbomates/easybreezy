package io.easybreezy.user.application.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.infrastructure.ktor.auth.isActivities
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
    suspend fun onCreate(command: Create): List<Error> {
        return transactionManager {
            validate(command) {
                validate(Create::email).isNotNull().isNotBlank().isUniqueEmail()
                validate(Create::firstName).isNotNull().isNotBlank()
                validate(Create::lastName).isNotNull().isNotBlank()
                validate(Create::activities).isNotNull().isActivities()
            }
        }
    }

    suspend fun onInvite(command: Invite): List<Error> {
        return transactionManager {
            validate(command) {
                validate(Invite::email).isNotNull().isNotBlank().isUniqueEmail()
                validate(Invite::activities).isNotNull().isActivities()
            }
        }
    }

    fun onArchive(command: Archive): List<Error> {
        return validate(command) {
            validate(Archive::userId).isNotNull()
            validate(Archive::reason).isNotBlank()
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
                    validate(User.Contact::value).isNotBlank()
                    if (contact.type == Contacts.Type.EMAIL) {
                        validate(User.Contact::value).isEmail()
                    }
                }
        }
    }

    fun onUpdateActivities(command: UpdateActivities): List<Error> {
        return validate(command) {
            validate(UpdateActivities::userId).isNotNull()
            validate(UpdateActivities::activities).isNotNull().isActivities()
        }
    }

    fun onChangeUsername(command: ChangeUsername): List<Error> {
        return validate(command) {
            validate(ChangeUsername::username).isNotNull().isValidUsername().isUniqueUsername()
        }
    }

    private object UniqueEmail : Constraint {
        override val name: String
            get() = "User with this email already exist"
    }

    private fun <E> Validator<E>.Property<String?>.isUniqueEmail(): Validator<E>.Property<String?> =
        this.validate(UniqueEmail) { value ->
            repository.findByEmail(Email.create(value!!)) !is User
        }

    private object UniqueUsername : Constraint {
        override val name: String
            get() = "User with this username already exist"
    }

    private fun <E> Validator<E>.Property<String?>.isUniqueUsername(): Validator<E>.Property<String?> =
        this.validate(UniqueUsername) { value ->
            repository.findByUsername(value!!) !is User
        }

    private object ValidUsername : Constraint {
        override val name: String
            get() = "Username should not contain spaces"
    }

    private fun <E> Validator<E>.Property<String?>.isValidUsername(): Validator<E>.Property<String?> =
        this.validate(ValidUsername) { value ->
            !("""\s""".toRegex().containsMatchIn(value!!))
        }
}
