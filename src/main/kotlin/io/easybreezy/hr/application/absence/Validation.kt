package io.easybreezy.hr.application.absence

import com.google.inject.Inject
import io.easybreezy.user.infrastructure.UserRepository
import org.valiktor.Constraint
import org.valiktor.Validator
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.validate
import java.util.UUID

class Validation @Inject constructor(private val repository: UserRepository) {

    object UserExists : Constraint {
        override val name: String
            get() = "User with this id not found"
    }

    private fun <E> Validator<E>.Property<UUID?>.isUserExists(): Validator<E>.Property<UUID?> =
        this.validate(UserExists) { value ->
            repository.find(value!!) != null
        }

    fun onCreate(command: Create) {
        validate(command) {
            validate(Create::startedAt).isNotNull()
            validate(Create::endedAt).isNotNull()
            validate(Create::comment).isNotBlank()
            validate(Create::reason).isNotNull().isNotBlank()
            validate(Create::userId).isNotNull().isUserExists()
        }
    }

    fun onUpdate(command: Update) {
        TODO("Not implemented")
    }
}