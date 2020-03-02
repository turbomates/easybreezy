package io.easybreezy

import io.easybreezy.infrastructure.exposed.toUUID
import io.easybreezy.infrastructure.ktor.auth.Role
import io.easybreezy.user.model.Email
import io.easybreezy.user.model.EmailTable
import io.easybreezy.user.model.Status
import io.easybreezy.user.model.Users
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

internal fun Database.createMember(): UUID {
    return transaction(this) {
        val id = Users.insert {
            it[status] = Status.ACTIVE
            it[email[EmailTable.email]] = "member@gmail.com"
            it[roles] = setOf(Role.MEMBER)
        } get Users.id
        id.toUUID()
    }
}
