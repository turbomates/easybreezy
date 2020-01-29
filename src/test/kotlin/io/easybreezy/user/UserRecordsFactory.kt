package io.easybreezy.user

import io.easybreezy.infrastructure.extensions.toUUID
import io.easybreezy.user.model.Role
import io.easybreezy.user.model.Status
import io.easybreezy.user.model.User
import io.easybreezy.user.model.Users
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

internal class UserRecordsFactory {
//todo: get User object by wrap
    companion object {
        fun createAdmin(): UUID {
            return transaction {
                 val id = Users.insert {
                    it[status] = Status.ACTIVE
                    it[email] = "admin@gmail.com"
                    it[roles] = setOf(Role.ADMIN)
                    it[firstName] = "First Name"
                    it[lastName] = "Last Name"
                    it[hashedPassword] = User.Password.hash("123")
                } get Users.id

                id.toUUID()
            }
        }

        fun createMember(): UUID {
                val id = Users.insert {
                    it[status] = Status.ACTIVE
                    it[email] = "member@gmail.com"
                    it[roles] = setOf(Role.MEMBER)
                    it[firstName] = "First Name"
                    it[lastName] = "Last Name"
                    it[hashedPassword] = User.Password.hash("123")
                } get Users.id

                return id.toUUID()

        }
    }
}