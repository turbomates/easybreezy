package io.easybreezy.infrastructure.ktor

import org.valiktor.ConstraintViolation
import org.valiktor.ConstraintViolationException
import org.valiktor.Validator

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
        Error(
            constraint.constraint.messageKey,
            constraint.property,
            constraint.value
        )
    }
}