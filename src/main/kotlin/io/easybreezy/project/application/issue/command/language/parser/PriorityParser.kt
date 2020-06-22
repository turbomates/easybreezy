package io.easybreezy.project.application.issue.command.language.parser

import io.easybreezy.project.application.issue.command.language.ParsedElements
import kotlin.reflect.KProperty

class PriorityParser {

    operator fun getValue(parsed: ParsedElements, property: KProperty<*>): String? {
        parsed.translations.forEach {
            it.priority.forEach { (t, values) ->
                values.split(",").map { priority ->
                    if (parsed.text.contains(priority.trim())) {
                        return t
                    }
                }
            }
        }
        return null
    }
}