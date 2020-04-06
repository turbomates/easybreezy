package io.easybreezy.project.application.team.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.infrastructure.ktor.validate
import io.easybreezy.project.model.team.Repository
import org.valiktor.Constraint
import org.valiktor.Validator
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotNull
import java.util.*

class Validation @Inject constructor(
    private val repository: Repository
) {

    fun validate(command: NewTeam): List<Error> {

        return validate(command) {
            validate(NewTeam::name).hasSize(2, 25)
        }
    }

    fun validate(command: NewMember): List<Error> {

      return validate(command) {
            validate(NewMember::role).isNotNull().isRoleBelongs(command.team)
            validate(NewMember::user).isNotNull()
        }
    }

    fun validate(command: RemoveMember): List<Error> {

        return validate(command) {
            validate(RemoveMember::memberId).isNotNull().isNoTickets()
        }
    }

    fun validate(command: ChangeMemberRole): List<Error> {

        return validate(command) {
            validate(ChangeMemberRole::newRoleId).isRoleBelongs(command.team)
        }
    }

    object MemberIsWorking : Constraint {
        override val name: String
            get() = "This member is working"
    }
    private fun <E> Validator<E>.Property<UUID?>.isNoTickets() {
        this.validate(MemberIsWorking) { value ->
            value == null || repository.isNoActualTickets(value)
        }
    }
    object RoleIsInvalid : Constraint {
        override val name: String
            get() = "Invalid role for this team"
    }
    private fun <E> Validator<E>.Property<UUID?>.isRoleBelongs(team: UUID) {
        this.validate(RoleIsInvalid) { value ->
            value == null || repository.isRoleBelongs(team, value)
        }
    }
}
