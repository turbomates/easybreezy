package io.easybreezy.project.application.issue.command.language.parser

import io.easybreezy.project.application.issue.command.language.ParsedElements
import kotlin.reflect.KProperty

class ReassignParser {

    companion object {
        const val REASSIGN_IDENTIFIER = "(?<=->@).+?(?=\\s)"
    }

    operator fun getValue(parsed: ParsedElements, property: KProperty<*>): String? {
        return REASSIGN_IDENTIFIER.toRegex()
            .find(parsed.text)?.value
    }
}