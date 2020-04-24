package io.easybreezy.project.application.member.queryobject

import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.project.model.Projects
import io.easybreezy.project.model.team.Members
import io.easybreezy.project.model.team.Teams
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import java.util.*

class IsProjectMember(private val userId: UUID, private val slug: String) : QueryObject<Boolean> {
    override suspend fun getData(): Boolean =
        Members
            .innerJoin(Teams)
            .innerJoin(Projects)
            .select { Members.user eq userId and (Projects.slug eq slug) }.count() > 0
}

class IsTeamMember(private val userId: UUID, private val teamId: UUID) : QueryObject<Boolean> {
    override suspend fun getData(): Boolean =
        Members
            .innerJoin(Teams)
            .select { Members.user eq userId and (Teams.id eq teamId) }.count() > 0
}