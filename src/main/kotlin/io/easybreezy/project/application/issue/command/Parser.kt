package io.easybreezy.project.application.issue.command

import com.google.inject.Inject

class Parser @Inject constructor() {

    fun parse(text: String): Parsed {
        val titleRegex = """(.+[\n.!?])((.|\n)*)""".toRegex()

        val titleMatch = titleRegex.find(text) ?: throw IssueParserException("Could not find a title!")
        val (title, other) = titleMatch.destructured

        return Parsed(title.trim(), other)
    }
}

data class Parsed(
    val title: String,
    val description: String
)

class IssueParserException(override val message: String): Exception(message)