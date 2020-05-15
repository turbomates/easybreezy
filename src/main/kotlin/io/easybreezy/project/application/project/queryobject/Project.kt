package io.easybreezy.project.application.project.queryobject

import io.easybreezy.infrastructure.query.ContinuousList
import io.easybreezy.infrastructure.query.PagingParameters
import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.query.toContinuousList
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import io.easybreezy.project.model.Projects
import io.easybreezy.project.model.issue.Categories
import io.easybreezy.project.model.issue.Statuses
import io.easybreezy.project.model.team.Members
import io.easybreezy.project.model.team.Roles
import io.easybreezy.project.model.team.Teams
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

class ProjectQO(private val slug: String) : QueryObject<Project> {
    override suspend fun getData(): Project {
        return Projects
            .leftJoin(Roles)
            .leftJoin(Categories)
            .leftJoin(Statuses)
            .join(Teams, JoinType.LEFT, Projects.id, Teams.project)
            .select {
                Projects.slug eq slug
            }
            .orderBy(Teams.name to SortOrder.ASC, Categories.name to SortOrder.ASC, Roles.name to SortOrder.ASC)
            .toProjectJoined()
            .single()
    }
}

class MyProjectsQO(private val user: UUID, private val paging: PagingParameters) :
    QueryObject<ContinuousList<Project>> {
    override suspend fun getData() =
        Projects
            .leftJoin(Teams)
            .leftJoin(Members)
            .select { (Projects.author eq user) or (Members.user eq user) }
            .toContinuousList(paging, ResultRow::toProject)
}

class ProjectsQO(private val paging: PagingParameters) : QueryObject<ContinuousList<Project>> {
    override suspend fun getData() =
        Projects
            .selectAll()
            .toContinuousList(paging, ResultRow::toProject)
}

fun Iterable<ResultRow>.toProjectJoined(): List<Project> {
    return fold(mutableMapOf<String, Project>()) { map, resultRow ->
        val project = resultRow.toProject()
        val current = map.getOrDefault(project.slug, project)

        val roleId = resultRow.getOrNull(Roles.id)
        val roles = roleId?.let { resultRow.toRole() }

        val teamId = resultRow.getOrNull(Teams.id)
        val teams = teamId?.let { resultRow.toTeam() }

        val categoryId = resultRow.getOrNull(Categories.id)
        val categories = categoryId?.let { resultRow.toCategory() }

        val statusId = resultRow.getOrNull(Statuses.id)
        val statuses = statusId?.let { resultRow.toStatus() }

        map[project.slug] = current.copy(
            roles = current.roles.plus(listOfNotNull(roles)).distinct(),
            teams = current.teams.plus(listOfNotNull(teams)).distinct(),
            categories = current.categories.plus(listOfNotNull(categories)).distinct(),
            statuses = current.statuses.plus(listOfNotNull(statuses)).distinct()
        )
        map
    }.values.toList()
}

fun ResultRow.toProject() = Project(
    this[Projects.id].value,
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

fun ResultRow.toCategory() = Category(
    this[Categories.id].value,
    this[Categories.name],
    this[Categories.parent]
)

fun ResultRow.toStatus() = Status(
    this[Statuses.id].value,
    this[Statuses.name]
)

@Serializable
data class Project(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val slug: String,
    val name: String,
    val status: String,
    val description: String?,
    var roles: List<Role> = listOf(),
    var teams: List<Team> = listOf(),
    var categories: List<Category> = listOf(),
    var statuses: List<Status> = listOf()
)

@Serializable
data class Role(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val permissions: List<io.easybreezy.project.model.team.Role.Permission>
)

@Serializable
data class Team(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String
)

@Serializable
data class Category(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    @Serializable(with = UUIDSerializer::class)
    val parent: UUID?
)

@Serializable
data class Status(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String
)
