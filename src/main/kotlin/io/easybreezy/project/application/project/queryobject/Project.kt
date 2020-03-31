package io.easybreezy.project.application.project.queryobject

import io.easybreezy.hr.model.hr.Employees
import io.easybreezy.infrastructure.query.ContinuousList
import io.easybreezy.infrastructure.query.PagingParameters
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.query.toContinuousList
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import io.easybreezy.project.model.Projects
import io.easybreezy.project.model.team.Roles
import io.easybreezy.project.model.team.Teams
import io.easybreezy.user.model.Contacts
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class ProjectQO(private val slug: String) : QueryObject<Project> {
    override suspend fun getData(): Project {
        return transaction {
            Projects
            .leftJoin(Roles)
            .join(Teams, JoinType.LEFT, Projects.id, Teams.project)
            .select {
                Projects.slug eq slug
            }
            .toProjectJoined()
            .single()
        }
    }
}

class ProjectsQO(private val paging: PagingParameters) : QueryObject<ContinuousList<Project>> {
    override suspend fun getData() =
        transaction {
            Projects
                .selectAll()
                .limit(paging.pageSize, paging.offset)
                .map { it.toProject() }
                .toContinuousList(paging.pageSize, paging.currentPage)
        }
}

fun Iterable<ResultRow>.toProjectJoined(): List<Project> {
    return fold(mutableMapOf<String, Project>()) { map, resultRow ->
        val project = resultRow.toProject()
        val current = map.getOrDefault(project.slug, project)

        val roleId = resultRow.getOrNull(Roles.id)
        val roles = roleId?.let { resultRow.toRole() }

        val teamId = resultRow.getOrNull(Teams.id)
        val teams = teamId?.let { resultRow.toTeam() }

        map[project.slug] = current.copy(
            roles = current.roles.plus(listOfNotNull(roles)).distinct(),
            teams = current.teams.plus(listOfNotNull(teams)).distinct()
        )
        map
    }.values.toList()
}

fun ResultRow.toProject() = Project(
    this[Projects.slug],
    this[Projects.name],
    this[Projects.status].name,
    this[Projects.description]
)

fun ResultRow.toRole() = Role(
    this[Roles.id].value,
    this[Roles.name],
    this[Roles.permissions]
)

fun ResultRow.toTeam() = Team(
    this[Teams.id].value,
    this[Teams.name]
)

@Serializable
data class Project(
    val slug: String,
    val name: String,
    val status: String,
    val description: String?,
    var roles: List<Role> = listOf(),
    var teams: List<Team> = listOf()
)

@Serializable
data class Role(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val permissions: List<String>
)

@Serializable
data class Team(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String
)
