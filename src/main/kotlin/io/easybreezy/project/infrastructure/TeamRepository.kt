package io.easybreezy.project.infrastructure

import io.easybreezy.project.model.team.Repository
import io.easybreezy.project.model.team.Roles
import io.easybreezy.project.model.team.Team
import io.easybreezy.project.model.team.Teams
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class TeamRepository : Repository, Team.Repository() {
    override fun isRoleBelongs(toTeam: UUID, role: UUID): Boolean {
        return transaction {
            Teams
                .join(Roles, JoinType.INNER, Teams.project, Roles.project)
                .select {
                    Roles.id eq role and (Teams.id eq toTeam)
                }
                .count() > 0
        }
    }

    override fun isNoActualTickets(member: UUID): Boolean {
        return true // @TODO implement
    }
}
