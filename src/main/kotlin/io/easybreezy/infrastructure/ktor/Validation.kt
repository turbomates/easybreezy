package io.easybreezy.infrastructure.ktor

import org.valiktor.ConstraintViolation
import org.valiktor.ConstraintViolationException
import org.valiktor.Validator
import org.valiktor.i18n.toMessage

fun <E> validate(obj: E, block: Validator<E>.(E) -> Unit): List<Error> {
    return try {
        org.valiktor.validate(obj, block)
        emptyList()
    } catch (ex: ConstraintViolationException) {
        ex.constraintViolations.toErrorsList()
    }
}

fun <E : ConstraintViolation> Set<E>.toErrorsList(): List<Error> {
    return map { constraint ->
        val message = constraint.toMessage()

        val errorMessage = when {
            message.message.isBlank() -> constraint.constraint.name
            else -> message.message
        }

        Error(
            errorMessage,
            message.property,
            message.value
        )
    }
}
