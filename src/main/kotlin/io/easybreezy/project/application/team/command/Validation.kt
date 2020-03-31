package io.easybreezy.project.application.team.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.infrastructure.ktor.validate
import io.easybreezy.project.model.team.Repository
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotNull
import java.util.*

class Validation @Inject constructor(
    private val repository: Repository
){

    fun validate(command: NewTeam): List<Error> {

        return validate(command) {
            validate(NewTeam::name).hasSize(2, 25)
        }
    }

    fun validate(command: NewMember): List<Error> {

      return validate(command) {
            validate(NewMember::role).isNotNull() //@todo add validation for current project
            validate(NewMember::user).isNotNull()
        }
    }

    fun validate(command: RemoveMember): List<Error> {

        return validate(command) {

        }
    }

    fun validate(command: ChangeMemberRole): List<Error> {

        return validate(command) {

        }
    }
}
