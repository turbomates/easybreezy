package io.easybreezy.project.application.project.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.project.model.Project

class Handler @Inject constructor(
    private val transaction: TransactionManager,
    private val repository: Project.Repository
) {
    suspend fun new(new: New) {
        transaction {
            Project.new(new.name, new.description)
        }
    }

    suspend fun addRole(role: NewRole) {
        transaction {
            val project = repository[role.projectID]
            project.addRole(role.name, role.permissions)
        }
    }
}