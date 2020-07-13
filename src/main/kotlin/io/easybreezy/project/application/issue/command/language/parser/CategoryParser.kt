package io.easybreezy.project.application.issue.command.language.parser

import io.easybreezy.project.application.issue.command.language.ParsedFields
import kotlin.reflect.KProperty

class CategoryParser {

    companion object {
        const val CATEGORY_IDENTIFIER = "(?<=<).+?(?=>)"
    }

    operator fun getValue(parsed: ParsedFields, property: KProperty<*>): String? {
        return CATEGORY_IDENTIFIER.toRegex()
            .find(parsed.text)?.value
    }
}
