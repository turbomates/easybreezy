package io.easybreezy.project.application.issue.command.parser

import com.google.inject.Inject
import kotlin.math.max

class Parser @Inject constructor() {

    companion object {
        const val PERSON_IDENTIFIER = "(?<=@).+?(?=\\s)"
        const val CATEGORY_IDENTIFIER = "(?<=<).+?(?=>)"
        const val TITLE_IDENTIFIER = "(.+[\\n.!?])((.|\\n)*)"
        const val TITLE_NO_IDENTIFIER_LENGTH = 10
        const val PRIORITY_IDENTIFIER = "(?<=\\s)\\w+?(?= priority)"
    }

    fun parse(text: String): Parsed {
        val (title, description) = extractTitle(text)

        val persons = extractPersons(description)
        return Parsed(
            title.trim(),
            description.trim(),
            extractPriority(description),
            extractCategory(description),
            persons.firstOrNull(),
            persons.drop(1).distinct().toList()
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

    private fun extractPriority(description: String): String? {
        return PRIORITY_IDENTIFIER.toRegex()
            .find(description)?.value
    }
}

data class Parsed(
    val title: String,
    val description: String,
    val priority: String? = null,
    val category: String? = null,
    val assignee: String? = null,
    val watchers: List<String>? = null
)
