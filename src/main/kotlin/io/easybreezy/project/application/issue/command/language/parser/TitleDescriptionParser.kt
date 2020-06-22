package io.easybreezy.project.application.issue.command.language.parser

import io.easybreezy.project.application.issue.command.language.ParsedElements
import kotlin.reflect.KProperty

class TitleDescriptionParser {

    companion object {
        const val TITLE_IDENTIFIER = "(.+[\\n.!?])((.|\\n)*)"
        const val TITLE_NO_IDENTIFIER_LENGTH = 10
    }

    operator fun getValue(parsed: ParsedElements, property: KProperty<*>): TitleDescription {
        val titleRegex = TITLE_IDENTIFIER.toRegex()
        val titleMatch = titleRegex.find(parsed.text)
        if (titleMatch != null) {
            val (title, description) = titleMatch.destructured
            return TitleDescription(title, description)
        }

        return TitleDescription(
            parsed.text.take(TITLE_NO_IDENTIFIER_LENGTH),
            parsed.text
        )
    }
}

data class TitleDescription(val title: String, val description: String)