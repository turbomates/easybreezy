package io.easybreezy.integration.gitlab.webhook.action

import io.easybreezy.integration.gitlab.webhook.action.Author
import java.time.LocalDate

class Commit(
    val id: String,
    val message: String,
    val timestamp: LocalDate,
    val URL: String,
    val author: Author,
    val added: List<String?> = emptyList(),
    val modified: List<String?> = emptyList(),
    val removed: List<String?> = emptyList()
)