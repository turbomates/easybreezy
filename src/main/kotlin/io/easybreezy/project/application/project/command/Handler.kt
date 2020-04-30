package io.easybreezy.project.application.project.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.project.model.Project
import io.easybreezy.project.model.Repository
import io.easybreezy.project.model.team.Role

class Handler @Inject constructor(
    private val transaction: TransactionManager,
    private val repository: Repository
) {
    suspend fun new(command: New) {
        transaction {
            Project.new(command.author, command.name, command.description, command.slug)
        }
    }

    suspend fun changeSlug(command: ChangeSlug) {
        transaction {
            project(command.slug).changeSlug(command.new)
        }
    }

    suspend fun activate(slug: String) {
        transaction {
            project(slug).activate()
        }
    }

    suspend fun close(slug: String) {
        transaction {
            project(slug).close()
        }
    }

    suspend fun suspendProject(slug: String) {
        transaction {
            project(slug).suspend()
        }
    }

    suspend fun writeDescription(command: WriteDescription) {
        transaction {
            project(command.project).writeDescription(command.description)
        }
    }

    suspend fun addRole(command: NewRole) {
        transaction {
            project(command.project).addRole(command.name, command.permissions.map { Role.Permission.valueOf(it) })
        }
    }

    suspend fun changeRole(command: ChangeRole) {
        transaction {
            project(command.project).changeRole(
                command.roleId,
                command.permissions.map { Role.Permission.valueOf(it) },
                command.name
            )
        }
    }

    suspend fun removeRole(command: RemoveRole) {
        transaction {
            project(command.project).removeRole(command.roleId)
        }
    }

    suspend fun addCategory(command: NewCategory) {
        transaction {
            project(command.project).addCategory(command.name, command.parent)
        }
    }

    suspend fun changeCategory(command: ChangeCategory) {
        transaction {
            project(command.project).changeCategory(command.categoryId, command.parent, command.name)
        }
    }

    suspend fun removeCategory(command: RemoveCategory) {
        transaction {
            project(command.project).removeCategory(command.categoryId)
        }
    }

    private fun project(slug: String) = repository.getBySlug(slug)
}
