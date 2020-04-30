package io.easybreezy.project.application.member.queryobject

import io.easybreezy.infrastructure.ktor.auth.Activity
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.project.model.Projects
import io.easybreezy.project.model.team.Members
import io.easybreezy.project.model.team.Roles
import io.easybreezy.project.model.team.Teams
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import java.util.UUID

class MemberActivities(private val userId: UUID, private val slug: String) : QueryObject<Set<Activity>> {
    override suspend fun getData(): Set<Activity> {
        val list = Teams
            .innerJoin(Members)
            .innerJoin(Projects)
            .leftJoin(Roles)
            .select { Members.user eq userId and (Projects.slug eq slug) }.flatMap {
                it[Roles.permissions]
            }.map { it.activity }.toSet()
        if (list.isEmpty()) {
            return emptySet()
        }
        return list
    }
}

class IsTeamMember(private val userId: UUID, private val teamId: UUID) : QueryObject<Boolean> {
    override suspend fun getData(): Boolean =
        Members
            .innerJoin(Teams)
            .select { Members.user eq userId and (Teams.id eq teamId) }.count() > 0
}
