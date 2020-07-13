package io.easybreezy.project.application.issue.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.validate
import org.valiktor.functions.isNotBlank
import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.project.application.project.queryobject.ProjectHasStatus
import io.easybreezy.project.model.issue.File
import org.valiktor.Constraint
import org.valiktor.functions.isNotNull
import org.valiktor.functions.validateForEach

class Validation @Inject constructor(
    private val queryExecutor: QueryExecutor
) {

    fun validateCommand(command: New): List<Error> {

        return validate(command) {
            validate(New::content).isNotBlank()
        }
    }

    fun validateCommand(command: AddComment): List<Error> {

        return validate(command) {
            validate(AddComment::content).isNotBlank()
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

    fun validateCommand(command: AddFiles): List<Error> {
        return validate(command) {
            validate(AddFiles::files).validateForEach {
                validate(File::name).isNotNull().isNotBlank()
                validate(File::encodedContent).isNotNull().isNotBlank()
                validate(File::extension).isNotNull().isNotBlank()
            }
        }
    }

    fun validateCommand(command: RemoveFile): List<Error> {
        return validate(command) {
            validate(RemoveFile::issueId).isNotNull()
            validate(RemoveFile::path).isNotNull().isNotBlank()
        }
    }

    private object StatusNotFound : Constraint {
        override val name: String
            get() = "Status not found"
    }
}
