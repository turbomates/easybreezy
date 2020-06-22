package io.easybreezy.project.application.issue.command.language.parser

import io.easybreezy.project.application.issue.command.language.ParsedElements
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.KProperty

class DueDateParser {

    companion object {
        const val DATE_IDENTIFIER = "[0-9]{2}/[0-9]{2}/[0-9]{4}(\\s[0-9]{2}:[0-9]{2})?"
    }

    operator fun getValue(parsed: ParsedElements, property: KProperty<*>): LocalDateTime? {
        val dates = DATE_IDENTIFIER.toRegex()
            .findAll(parsed.text).toList()
            .map { dateStr ->
                val datetime = dateStr.value.split(" ")
                LocalDate
                    .parse(datetime.first(), DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    .atTime(
                        LocalTime.parse(datetime.getOrNull(1) ?: "00:00", DateTimeFormatter.ofPattern("HH:mm"))
                    )
            }
            .sortedDescending()

        return dates.lastOrNull()
    }
}