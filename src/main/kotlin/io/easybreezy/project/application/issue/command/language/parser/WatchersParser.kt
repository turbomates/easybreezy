package io.easybreezy.project.application.issue.command.language.parser

import io.easybreezy.project.application.issue.command.language.ParsedElements
import kotlin.reflect.KProperty

class WatchersParser {

    companion object {
        const val WATCHERS_IDENTIFIER = "(?<=@).+?(?=\\s)"
    }

    operator fun getValue(parsed: ParsedElements, property: KProperty<*>): List<String> {
        return WATCHERS_IDENTIFIER.toRegex()
            .findAll(parsed.text)
            .map { it.value.trim() }
            .toList()
    }
}