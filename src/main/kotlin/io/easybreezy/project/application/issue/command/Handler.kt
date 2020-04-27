package io.easybreezy.project.application.issue.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.project.model.Repository as ProjectRepository
import io.easybreezy.project.model.issue.Issue

class Handler @Inject constructor(
    private val transaction: TransactionManager,
    private val parser: Parser,
    private val projectRepository: ProjectRepository
){

    suspend fun newIssue(command: New) {
        val parsed = parser.parse(command.content)
        val project = projectRepository.getBySlug(command.project).id.value
        transaction {
            Issue.create(
                command.author,
                project,
                parsed.title,
                parsed.description
            )
        }
    }

    suspend fun update(command: Update) {
        throw NotImplementedError()
    }
}