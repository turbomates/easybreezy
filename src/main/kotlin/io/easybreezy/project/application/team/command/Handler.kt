package io.easybreezy.project.application.team.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.project.model.team.Team
import io.easybreezy.project.model.team.Repository
import java.util.*

class Handler @Inject constructor(
    private val transaction: TransactionManager,
    private val repository: Repository
) {

    suspend fun newTeam(command: NewTeam) {
        transaction {
            Team.create(command.name, command.project)
        }
    }

    suspend fun newMember(team: UUID, command: NewMember) {
        transaction {
            team(team).addMember(command.user, command.role)
        }
    }

    suspend fun close(team: UUID) {
        transaction {
            team(team).close()
        }
    }

    suspend fun activate(team: UUID) {
        transaction {
            team(team).activate()
        }
    }

    suspend fun removeMember(team: UUID, command: RemoveMember) {
        transaction {
            team(team).removeMember(command.memberId)
        }
    }

    suspend fun changeMemberRole(team: UUID, command: ChangeMemberRole) {
        transaction {
            team(team).changeMemberRole(command.memberId, command.newRoleId)
        }
    }

    private fun team(team: UUID) = repository.get(team)
}
