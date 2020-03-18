package io.easybreezy.infrastructure.query

import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import io.ktor.request.ApplicationRequest
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
class DateRange(
    @Serializable(with = LocalDateSerializer::class)
    val from: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val to: LocalDate
)

fun ApplicationRequest.extractDateRange(): DateRange {
    val from =
        if (queryParameters["from"] != null) LocalDate.parse(queryParameters["from"])
        else LocalDate.now().minusYears(1)

    val to =
        if (queryParameters["to"] != null) LocalDate.parse(queryParameters["to"])
        else LocalDate.now().plusMonths(6)

    return DateRange(from, to)
}
