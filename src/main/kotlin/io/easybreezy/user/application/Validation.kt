package io.easybreezy.user.application

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.infrastructure.ktor.validate
import io.easybreezy.user.model.Contacts
import io.easybreezy.user.model.Email
import io.easybreezy.user.model.Repository
import io.easybreezy.user.model.User
import org.valiktor.Constraint
import org.valiktor.Validator
import org.valiktor.functions.*

class Validation @Inject constructor(private val repository: Repository) {

    object Unique : Constraint {
        override val name: String
            get() = "User with this email already exist"
    }

    private fun <E> Validator<E>.Property<String?>.isUnique(): Validator<E>.Property<String?> =
        this.validate(Unique) { value ->
            repository.findByEmail(Email.create(value!!)) !is User
        }

    fun onInvite(command: Invite): List<Error> {
        return validate(command) {
            validate(Invite::email).isNotBlank().isNotNull().isUnique()
        }
    }

    fun onUpdateContacts(command: UpdateContacts): List<Error> {
        return validate(command) {
            validate(UpdateContacts::contacts)
                .validateForEach {
                    validate(Contact::type).isIn(Contacts.Type.stringValues())
                    validate(Contact::value).isNotBlank()
                    //isEmailContact() @todo
                }
        }
    }

    private fun <E> Validator<E>.Property<Contact?>.isEmailContact(): Validator<E>.Property<Contact?> =
        this.validate(org.valiktor.constraints.Email) { contact ->
            contact == null ||
            contact.type != Contacts.Type.EMAIL.toString() ||
            !contact.value.matches(
                Regex("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
            )
        }
}
