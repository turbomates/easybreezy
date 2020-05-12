package io.easybreezy.project.application.issue.command.parser

import com.google.inject.Inject

class Parser @Inject constructor() {

    companion object {
        const val PERSON_IDENTIFIER = "(?<=@).+?(?=\\s)"
        const val CATEGORY_IDENTIFIER = "(?<=<).+?(?=>)"
        const val TITLE_DESC_IDENTIFIER = "(.+[\\n.!?])((.|\\n)*)"
        const val PRIORITY_IDENTIFIER = "(?<=\\s)\\w+?(?= priority)"
    }

    fun parse(text: String): Parsed {
        val titleRegex = TITLE_DESC_IDENTIFIER.toRegex()

        val titleMatch = titleRegex.find(text) ?: throw IssueParserException(
            "Could not find a title!"
        )
        val (title, description) = titleMatch.destructured

        val persons = extractPersons(description)
        return Parsed(
            title.trim(),
            description,
            extractPriority(description),
            extractCategory(description),
            persons.firstOrNull(),
            persons.drop(1).distinct().toList()
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

class IssueParserException(override val message: String) : Exception(message)
