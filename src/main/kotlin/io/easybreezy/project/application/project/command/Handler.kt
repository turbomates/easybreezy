package io.easybreezy.project.application.project.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.project.model.Project
import io.easybreezy.project.model.Repository
import java.util.*

class Handler @Inject constructor(
    private val transaction: TransactionManager,
    private val repository: Repository
) {
    suspend fun new(new: New, author: UUID) {
        transaction {
            Project.new(author, new.name, new.description)
        }
    }

    suspend fun addRole(command: NewRole, slug: String) {
        transaction {
            val project = repository.getBySlug(slug)
            project.addRole(command.name, command.permissions)
        }
    }

    suspend fun addTeam(command: NewTeam, slug: String) {
        transaction {
            val project = repository.getBySlug(slug)
            project.createTeam(command.name)
        }
    }
}
