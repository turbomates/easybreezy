package io.easybreezy.project.application.project.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.infrastructure.ktor.validate
import io.easybreezy.project.model.Repository
import org.valiktor.Constraint
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotEmpty

class Validation @Inject constructor(private val repository: Repository){
    fun validate(command: New): List<Error> {

        return validate(command) {
            validate(New::name).hasSize(2, 255)
            validate(New::description).isNotBlank()
        }
    }

    fun validate(command: NewRole): List<Error> {

        return validate(command) {
            validate(NewRole::name).hasSize(2, 25)
            validate(NewRole::permissions).isNotEmpty()
        }
    }

    fun validate(command: ChangeRole): List<Error> {

        return validate(command) {
            validate(ChangeRole::name).hasSize(0, 25)
            validate(ChangeRole::permissions).isNotEmpty()
        }
    }


    object HasMembers : Constraint {
        override val name: String
            get() = "There are members with this role"
    }

    fun validate(command: RemoveRole): List<Error> {

        if (repository.hasMembers(command.roleId)) {
            return listOf(Error(HasMembers.name))
        }
        return listOf()
    }

    fun validate(command: WriteDescription): List<Error> {

        return validate(command) {
            validate(WriteDescription::description).isNotBlank()
        }
    }
}
