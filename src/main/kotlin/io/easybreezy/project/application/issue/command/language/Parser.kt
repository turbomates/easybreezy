package io.easybreezy.project.application.issue.command.language

import com.google.inject.Inject
import io.easybreezy.project.application.issue.command.language.parser.AssigneeParser
import io.easybreezy.project.application.issue.command.language.parser.CategoryParser
import io.easybreezy.project.application.issue.command.language.parser.DueDateParser
import io.easybreezy.project.application.issue.command.language.parser.LabelsParser
import io.easybreezy.project.application.issue.command.language.parser.PriorityParser
import io.easybreezy.project.application.issue.command.language.parser.ReassignParser
import io.easybreezy.project.application.issue.command.language.parser.TitleDescriptionParser
import io.easybreezy.project.application.issue.command.language.parser.WatchersParser

class Parser @Inject constructor(
    translator: LanguageTranslator
) {
    private val translations: List<Translations> by lazy { translator.load() }

    fun parse(text: String): ParsedFields {
        return ParsedFields(text, translations)
    }
}

class ParsedFields(val text: String, val translations: List<Translations>) {
    val titleDescription by TitleDescriptionParser()
    val priority by PriorityParser()
    val category by CategoryParser()
    val assignee by AssigneeParser()
    val reassigned by ReassignParser()
    val watchers by WatchersParser()
    val labels by LabelsParser()
    val dueDate by DueDateParser()
}
