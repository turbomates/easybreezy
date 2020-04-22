package io.easybreezy.project.application.project.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.infrastructure.ktor.validate
import io.easybreezy.infrastructure.query.QueryExecutor
import io.easybreezy.project.application.issue.queryobject.HasIssuesQO
import io.easybreezy.project.model.Repository
import org.valiktor.Constraint
import org.valiktor.Validator
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotEmpty
import java.util.*

class Validation @Inject constructor(
    private val transactionManager: TransactionManager,
    private val repository: Repository,
    private val queryExecutor: QueryExecutor
) {
    fun validateCommand(command: New): List<Error> {

        return validate(command) {
            validate(New::name).hasSize(2, 255)
            validate(New::description).isNotBlank()
        }
    }

    fun validateCommand(command: NewRole): List<Error> {

        return validate(command) {
            validate(NewRole::name).hasSize(2, 25)
            validate(NewRole::permissions).isNotEmpty()
        }
    }

    fun validateCommand(command: ChangeRole): List<Error> {

        return validate(command) {
            validate(ChangeRole::name).hasSize(2, 25)
            validate(ChangeRole::permissions).isNotEmpty()
        }
    }

    object HasMembers : Constraint {
        override val name: String
            get() = "There are members with this role"
    }

    suspend fun validateCommand(command: RemoveRole): List<Error> {
        return transactionManager {
            if (repository.hasMembers(command.roleId)) {
                listOf(Error(HasMembers.name))
            } else {
                listOf<Error>()
            }
        }
    }

    fun validateCommand(command: WriteDescription): List<Error> {

        return validate(command) {
            validate(WriteDescription::description).isNotBlank()
        }
    }

    object NoParentCategory : Constraint {
        override val name: String
            get() = "No category found"
    }

    suspend fun validateCommand(command: NewCategory): List<Error> {
        return transactionManager {
            validate(command) {
                validate(NewCategory::name).hasSize(2, 25)
                validate(NewCategory::parent).isNullOrProjectCategory(command.project)
            }
        }
    }

    suspend fun validateCommand(command: ChangeCategory): List<Error> {
        return transactionManager {
            validate(command) {
                validate(ChangeCategory::name).hasSize(2, 25)
                validate(ChangeCategory::parent).isNullOrProjectCategory(command.project)
            }
        }
    }

    private fun <E> Validator<E>.Property<UUID?>.isNullOrProjectCategory(project: String) {
        this.validate(NoParentCategory) { value ->
            value == null || repository.isProjectCategory(value, project)
        }
    }

    object HasIssues : Constraint {
        override val name: String
            get() = "There are issues with this category"
    }

    suspend fun validateCommand(command: RemoveCategory): List<Error> {
        if (queryExecutor.execute(HasIssuesQO(command.categoryId))) {
            return listOf(Error(HasIssues.name))
        }
        return listOf()
    }

}
