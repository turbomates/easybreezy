package io.easybreezy.project.application.issue.command

import com.google.inject.Inject

//@TODO write parser
class Parser @Inject constructor() {

    fun parse(text: String) : Parsed {
        return Parsed(text.substring(0, 10), text.substring(11))
    }
}

data class Parsed(
    val title: String,
    val description: String
)