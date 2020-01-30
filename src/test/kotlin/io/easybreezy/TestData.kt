package io.easybreezy

import io.easybreezy.infrastructure.extensions.toUUID
import io.easybreezy.user.model.Role
import io.easybreezy.user.model.Status
import io.easybreezy.user.model.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

internal fun Database.createAdmin(): UUID {
    return transaction(this) {
        val id = Users.insert {
            it[status] = Status.ACTIVE
            it[email] = "admin@gmail.com"
            it[roles] = setOf(Role.ADMIN)
        } get Users.id
        id.toUUID()
    }
}

internal fun Database.createMember(): UUID {
    return transaction(this) {
        val id = Users.insert {
            it[status] = Status.ACTIVE
            it[email] = "member@gmail.com"
            it[roles] = setOf(Role.MEMBER)
        } get Users.id
        id.toUUID()
    }
}
