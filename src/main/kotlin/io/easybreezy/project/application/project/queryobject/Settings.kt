package io.easybreezy.project.application.project.queryobject

import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.project.model.issue.Categories
import io.easybreezy.project.model.issue.Statuses
import io.easybreezy.project.model.team.Members
import io.easybreezy.project.model.team.Teams
import io.easybreezy.user.model.Users
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.lowerCase
import java.util.UUID

class IssueStatusOnCreateQO(private val project: UUID) : QueryObject<UUID?> {
    override suspend fun getData(): UUID? {
        return Statuses
            .select {
                Statuses.project eq project
            }
            .firstOrNull()?.let {
                it[Statuses.id].value
            }
    }
}

class CategoryQO(private val project: UUID, private val name: String) : QueryObject<UUID?> {
    override suspend fun getData(): UUID? {
        val name = name.toLowerCase()
        return Categories
            .select {
                Categories.project eq project and (Categories.name.lowerCase() eq name)
            }
            .firstOrNull()?.let {
                it[Categories.id].value
            }
    }
}

class MembersQO(private val project: UUID, private val usernames: List<String>) : QueryObject<List<UUID>?> {
    override suspend fun getData(): List<UUID>? {
        return Members
            .join(Users, JoinType.INNER, Members.user, Users.id)
            .join(Teams, JoinType.INNER, Members.team, Teams.id)
            .select {
                Teams.project eq project and (Users.username.lowerCase() inList usernames)
            }
            .map { it[Users.id].value }
    }
}

class LastLowestPriorityQO(private val project: UUID) : QueryObject<Int?> {
    override suspend fun getData(): Int? {
        return 0 // @todo select min priority where project
    }
}

class LastHighestPriorityQO(private val project: UUID) : QueryObject<Int?> {
    override suspend fun getData(): Int? {
        return 0 // @todo select max priority where project
    }
}
