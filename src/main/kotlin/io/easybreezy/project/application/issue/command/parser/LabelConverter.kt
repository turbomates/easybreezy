package io.easybreezy.project.application.issue.command.parser

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.project.infrastructure.LabelRepository
import io.easybreezy.project.model.issue.Label

class LabelConverter @Inject constructor(
    private val transaction: TransactionManager,
    private val repository: LabelRepository
) {
    suspend fun convert(names: List<String>): List<Label> {

        return transaction {
            val existed = repository.findByNames(names)
            val new = names.filter { it !in existed.map { label -> label.name } }
            val labels = existed.toMutableList()
            new.forEach { labels.add(Label.new(it)) }
            labels.toList()
        }
    }
}
