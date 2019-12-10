package io.easybreezy.infrastructure.ktor

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import org.valiktor.ConstraintViolationException

object ErrorRenderer {
    suspend fun render(call: ApplicationCall, exception: ConstraintViolationException) {
        call.respondWith(HttpStatusCode.UnprocessableEntity) {
            errors = exception.constraintViolations.map {
                mapOf(
                    "property" to it.property,
                    "value" to it.value,
                    "message" to it.constraint.name
                )
            }
        }
    }

    suspend fun render(call: ApplicationCall, exception: Exception) {
        call.respondWith(HttpStatusCode.UnprocessableEntity) {
            error = exception.message
        }
    }

    suspend fun render(call: ApplicationCall, message: String, statusCode: HttpStatusCode) {
        call.respondWith(statusCode) {
            error = message
        }
    }
}
