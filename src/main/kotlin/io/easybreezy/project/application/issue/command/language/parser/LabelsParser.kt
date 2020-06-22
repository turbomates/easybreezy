package io.easybreezy.project.application.issue.command.language.parser

import io.easybreezy.project.application.issue.command.language.ParsedElements
import kotlin.reflect.KProperty

class LabelsParser {

    companion object {
        const val LABEL_IDENTIFIER = "(?<=\\*)\\b.+?\\b(?=\\*)"
    }

    operator fun getValue(parsed: ParsedElements, property: KProperty<*>): List<String>? {
        return LABEL_IDENTIFIER.toRegex()
            .findAll(parsed.text).distinct().toList().map { it.value.toLowerCase() }
    }
}