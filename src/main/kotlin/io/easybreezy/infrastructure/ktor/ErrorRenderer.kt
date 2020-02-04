package io.easybreezy.infrastructure.ktor

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import org.valiktor.ConstraintViolationException

object ErrorRenderer {
    suspend fun render(call: ApplicationCall, exception: ConstraintViolationException) {
        call.respondError(HttpStatusCode.UnprocessableEntity, exception.constraintViolations.map {
            Error(it.constraint.name, it.property, it.value)
        })
    }

    suspend fun render(call: ApplicationCall, exception: Exception) {
        call.respondError(HttpStatusCode.UnprocessableEntity, Error(exception.message!!))
    }

    suspend fun render(call: ApplicationCall, message: String, statusCode: HttpStatusCode) {
        call.respondError(statusCode, Error(message))
    }
}
