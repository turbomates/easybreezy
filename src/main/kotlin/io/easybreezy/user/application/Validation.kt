package io.easybreezy.user.application

import com.google.inject.Inject
import io.easybreezy.user.model.Repository
import org.valiktor.Constraint
import org.valiktor.validate

class Validation @Inject constructor(private val repository: Repository) {

    object Unique : Constraint {
        override val name: String
            get() = "User with this email already exist"
    }

//    private fun <E> Validator<E>.Property<String?>.isUnique(): Validator<E>.Property<String?> =
//        this.validate(Unique) { value ->
//            repository.findByEmail(User.Email.create(value!!)) !is User
//        }

    fun onInvite(command: Invite) {
        validate(command) {
//            validate(Invite::email).isNotBlank().isNotNull().isUnique()
        }
    }
}
