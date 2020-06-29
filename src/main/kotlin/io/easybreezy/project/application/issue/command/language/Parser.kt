package io.easybreezy.project.application.issue.command.language

import com.google.inject.Inject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Parser @Inject constructor(
    translator: LanguageTranslator
) {
    private val translations: List<Translations> = translator.load()

    companion object {
        const val PERSON_IDENTIFIER = "(?<=@).+?(?=\\s)"
        const val REASSIGN_IDENTIFIER = "(?<=->@).+?(?=\\s)"
        const val CATEGORY_IDENTIFIER = "(?<=<).+?(?=>)"
        const val LABEL_IDENTIFIER = "(?<=\\*)\\b.+?\\b(?=\\*)"
        const val TITLE_IDENTIFIER = "(.+[\\n.!?])((.|\\n)*)"
        const val TITLE_NO_IDENTIFIER_LENGTH = 10
        const val DATE_IDENTIFIER = "[0-9]{2}/[0-9]{2}/[0-9]{4}(\\s[0-9]{2}:[0-9]{2})?"
    }

    fun parse(text: String): ParsedIssue {
        val (title, description) = extractTitle(text)
        val reassign = extractReassign(description)
        val persons = extractPersons(description)
        return ParsedIssue(
            title.trim(),
            description.trim(),
            extractPriority(description),
            extractCategory(description),
            reassign ?: persons.firstOrNull(),
            persons.drop(1).distinct().toList(),
            extractLabels(description),
            extractDueDate(description)
        )
    }

    private fun extractTitle(text: String): Pair<String, String> {
        val titleRegex = TITLE_IDENTIFIER.toRegex()
        val titleMatch = titleRegex.find(text)
        if (titleMatch != null) {
            val (title, description) = titleMatch.destructured
            return Pair(title, description)
        }

        return Pair(
            text.take(TITLE_NO_IDENTIFIER_LENGTH),
            text
        )
    }

    private fun extractPersons(description: String): Sequence<String> {
        return PERSON_IDENTIFIER.toRegex()
            .findAll(description)
            .map { it.value.trim() }
    }

    private fun extractReassign(description: String): String? {
        return REASSIGN_IDENTIFIER.toRegex()
            .find(description)?.value
    }

    private fun extractCategory(description: String): String? {
        return CATEGORY_IDENTIFIER.toRegex()
            .find(description)?.value
    }

    private fun extractLabels(description: String): List<String>? {
        return LABEL_IDENTIFIER.toRegex()
            .findAll(description).distinct().toList().map { it.value.toLowerCase() }
    }

    private fun extractPriority(description: String): String? {
        translations.forEach {
            it.priority.forEach { (t, values) ->
                values.split(",").map { priority ->
                    if (description.contains(priority.trim())) {
                        return t
                    }
                }
            }
        }
        return null
    }

    private fun extractDueDate(description: String): LocalDateTime? {
        val dates = DATE_IDENTIFIER.toRegex()
            .findAll(description).toList()
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

data class ParsedIssue(
    val title: String,
    val description: String,
    val priority: String? = null,
    val category: String? = null,
    val assignee: String? = null,
    val watchers: List<String>? = null,
    val labels: List<String>? = null,
    val due: LocalDateTime? = null
)
