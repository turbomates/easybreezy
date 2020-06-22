package io.easybreezy.project.application.issue.command.language.parser

import io.easybreezy.project.application.issue.command.language.ParsedElements
import kotlin.reflect.KProperty

class AssigneeParser {

    companion object {
        const val ASSIGNEE_IDENTIFIER = "(?<=@).+?(?=\\s)"
    }

    operator fun getValue(parsed: ParsedElements, property: KProperty<*>): String? {
        return ASSIGNEE_IDENTIFIER.toRegex()
            .find(parsed.text)?.value
    }
}