package io.easybreezy.project.application.issue.command.language.normalizer

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.project.application.issue.command.language.NormalizedElements
import io.easybreezy.project.application.issue.command.language.ParsedElements
import io.easybreezy.project.infrastructure.LabelRepository
import io.easybreezy.project.model.issue.Label
import java.util.UUID

class LabelNormalizer @Inject constructor(
    private val transaction: TransactionManager,
    private val repository: LabelRepository
) : ElementNormalizer {

    override suspend fun normalize(project: UUID, parsed: ParsedElements, normalizedElements: NormalizedElements): NormalizedElements {
        return normalizedElements.copy(
            labels = parsed.labels?.let { names ->
                transaction {
                    val existed = repository.findByNames(names)
                    val new = names.filter { it !in existed.map { label -> label.name } }
                    val labels = existed.toMutableList()
                    new.forEach { labels.add(Label.new(it)) }
                    labels.toList()
                }
            }
        )
    }
}
