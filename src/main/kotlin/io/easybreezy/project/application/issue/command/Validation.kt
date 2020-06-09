package io.easybreezy.project.application.issue.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.validate
import org.valiktor.functions.isNotBlank
import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.project.application.project.queryobject.ProjectHasStatus
import org.valiktor.Constraint

class Validation @Inject constructor(
    private val queryExecutor: QueryExecutor
) {

    fun validateCommand(command: New): List<Error> {

        return validate(command) {
            validate(New::content).isNotBlank()
        }
    }

    fun validateCommand(command: CommentUpdate): List<Error> {

        return validate(command) {
            validate(CommentUpdate::content).isNotBlank()
        }
    }

    suspend fun validateCommand(command: ChangeStatus): List<Error> {

        if (!queryExecutor.execute(ProjectHasStatus(command.project, command.newStatus))) {
            return listOf(Error(StatusNotFound.name))
        }
        return listOf()
    }

    fun validateCommand(command: CreateSubIssue): List<Error> {

        return validate(command) {
            validate(CreateSubIssue::content).isNotBlank()
        }
    }

    private object StatusNotFound : Constraint {
        override val name: String
            get() = "Status not found"
    }
}
