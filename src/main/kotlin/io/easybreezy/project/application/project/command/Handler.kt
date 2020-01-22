package io.easybreezy.project.application.project.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.project.model.Project

class Handler @Inject constructor(private val transaction: TransactionManager) {
    suspend fun new(new: New) {
        transaction {
            Project.new(new.name, new.description)
        }
    }
}