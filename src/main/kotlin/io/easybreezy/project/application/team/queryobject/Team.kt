package io.easybreezy.project.application.team.queryobject

import io.easybreezy.infrastructure.query.QueryObject
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import io.easybreezy.project.model.team.Members
import io.easybreezy.project.model.team.Teams
import io.easybreezy.user.model.EmailTable
import io.easybreezy.user.model.NameTable
import io.easybreezy.user.model.Users
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import java.util.*

class TeamQO(private val teamId: UUID) : QueryObject<Team> {
    override suspend fun getData() =
            Teams
            .leftJoin(Members)
            .join(Users, JoinType.LEFT, Members.user, Users.id)
            .select {
                Teams.id eq teamId
            }
            .toTeamJoined()
            .single()
}

fun Iterable<ResultRow>.toTeamJoined(): List<Team> {
    return fold(mutableMapOf<UUID, Team>()) { map, resultRow ->
        val team = resultRow.toTeam()
        val current = map.getOrDefault(team.id, team)

        val memberId = resultRow.getOrNull(Members.id)
        val members = memberId?.let { resultRow.toMember() }

        map[team.id] = current.copy(
            members = current.members.plus(listOfNotNull(members)).distinct()
        )
        map
    }.values.toList()
}

fun ResultRow.toTeam() = Team(
    this[Teams.id].value,
    this[Teams.name],
    this[Teams.status].name
)

fun ResultRow.toMember() = Member(
    this[Members.user],
    this[Members.role],
    this[Users.email[EmailTable.email]],
    this[Users.name[NameTable.firstName]],
    this[Users.name[NameTable.lastName]]
)

@Serializable
data class Team(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val status: String,
    var members: List<Member> = listOf()
)

@Serializable
data class Member(
    @Serializable(with = UUIDSerializer::class)
    val user: UUID,
    @Serializable(with = UUIDSerializer::class)
    val role: UUID,
    val email: String? = null,
    val first: String? = null,
    val last: String? = null
)
