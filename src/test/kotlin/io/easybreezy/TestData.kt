package io.easybreezy

import io.easybreezy.hr.model.hr.Employees
import io.easybreezy.infrastructure.exposed.toUUID
import io.easybreezy.infrastructure.ktor.auth.Role
import io.easybreezy.project.model.Project
import io.easybreezy.project.model.Projects
import io.easybreezy.project.model.team.Members
import io.easybreezy.project.model.team.Roles
import io.easybreezy.project.model.team.Teams
import io.easybreezy.user.model.EmailTable
import io.easybreezy.user.model.NameTable
import io.easybreezy.user.model.Status
import io.easybreezy.user.model.Users
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

internal fun Database.createAdmin(): UUID {
    return transaction(this) {
        val id = Users.insert {
            it[status] = Status.ACTIVE
            it[email[EmailTable.email]] = "admin@gmail.com"
            it[roles] = setOf(Role.ADMIN)
        } get Users.id
        id.toUUID()
    }
}

internal fun Database.createMember(firstName: String = "John", lastName: String = "Doe"): UUID {
    return transaction(this) {
        val id = Users.insert {
            it[status] = Status.ACTIVE
            it[email[EmailTable.email]] = "member@gmail.com"
            it[name[NameTable.firstName]] = firstName
            it[name[NameTable.lastName]] = lastName
            it[roles] = setOf(Role.MEMBER)
        } get Users.id
        id.toUUID()
    }
}

internal fun Database.createMyProject(): EntityID<UUID> {
    return transaction(this) {
        Projects.insert {
            it[slug] = "my-project"
            it[name] = "My Project"
            it[description] = "descr"
            it[author] = UUID.randomUUID()
            it[status] = Project.Status.Active
        } get Projects.id
    }
}

internal fun Database.createProjectRole(projectId: EntityID<UUID>, role: String): UUID {
    return transaction(this) {
        val id = Roles.insert {
            it[project] = projectId
            it[name] = role
            it[permissions] = listOf()
        } get Roles.id
        id.toUUID()
    }
}

internal fun Database.createProjectTeam(projectId: UUID, team: String): UUID {
    return transaction(this) {
        val id = Teams.insert {
            it[project] = projectId
            it[name] = team
            it[status] = io.easybreezy.project.model.team.Status.Active
        } get Teams.id
        id.toUUID()
    }
}

internal fun Database.createTeamMember(teamId: UUID, member: UUID, roleId: UUID) {
    return transaction(this) {
        Members.insert {
            it[team] = EntityID(teamId, Teams)
            it[user] = member
            it[role] = roleId
        }
    }
}

internal fun Database.createEmployee(): UUID {
    return transaction(this) {
        val id = Users.insert {
            it[status] = Status.ACTIVE
            it[email[EmailTable.email]] = "employee@gmail.com"
            it[roles] = setOf(Role.MEMBER)
        } get Users.id

        Employees.insert {
            it[userId] = id.value
        }

        id.toUUID()
    }
}
