package io.easybreezy.project.application.team.command

import com.google.inject.Inject
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.project.model.team.Team
import io.easybreezy.project.model.team.Team.Repository
import io.easybreezy.project.model.Repository as ProjectRepository
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
            repository[team].addMember(command.user, command.role)
        }
    }

    suspend fun close(team: UUID) {
        transaction {
            repository[team].close()
        }
    }

    suspend fun activate(team: UUID) {
        transaction {
            repository[team].activate()
        }
    }

    suspend fun removeMember(team: UUID, command: RemoveMember) {
        transaction {
            repository[team].removeMember(command.memberId)
        }
    }

    suspend fun changeMemberRole(team: UUID, command: ChangeMemberRole) {
        transaction {
            repository[team].changeMemberRole(command.memberId, command.newRoleId)
        }
    }
}
