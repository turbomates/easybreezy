package io.easybreezy.project.application.issue.command.parser

import com.google.inject.Inject
import java.time.LocalDateTime
import kotlin.math.max

class Parser @Inject constructor(
    translator: Translator
) {
    private val translations: List<Translations> = translator.load()

    companion object {
        const val PERSON_IDENTIFIER = "(?<=@).+?(?=\\s)"
        const val CATEGORY_IDENTIFIER = "(?<=<).+?(?=>)"
        const val LABEL_IDENTIFIER = "(?<=\\*)\\b.+?\\b(?=\\*)"
        const val TITLE_IDENTIFIER = "(.+[\\n.!?])((.|\\n)*)"
        const val TITLE_NO_IDENTIFIER_LENGTH = 10
    }

    fun parse(text: String): Parsed {
        val (title, description) = extractTitle(text)
        val persons = extractPersons(description)
        val dates = extractDates(description)
        return Parsed(
            title.trim(),
            description.trim(),
            extractPriority(description),
            extractCategory(description),
            persons.firstOrNull(),
            persons.drop(1).distinct().toList(),
            extractLabels(description),
            dates.first,
            dates.second
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
            text.takeLast(max(0, text.count() - TITLE_NO_IDENTIFIER_LENGTH))
        )
    }

    private fun extractPersons(description: String): Sequence<String> {
        return PERSON_IDENTIFIER.toRegex()
            .findAll(description)
            .map { it.value.trim() }
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

    private fun extractDates(description: String): Pair<LocalDateTime?, LocalDateTime?> {
        return Pair(LocalDateTime.now(), LocalDateTime.now())
    }
}

data class Parsed(
    val title: String,
    val description: String,
    val priority: String? = null,
    val category: String? = null,
    val assignee: String? = null,
    val watchers: List<String>? = null,
    val labels: List<String>? = null,
    val start: LocalDateTime? = null,
    val due: LocalDateTime? = null
)
