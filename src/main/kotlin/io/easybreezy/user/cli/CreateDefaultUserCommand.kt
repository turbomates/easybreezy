package io.easybreezy.user.cli

import io.easybreezy.application.HikariDataSource
import io.easybreezy.application.SystemConfiguration
import io.easybreezy.user.model.Email
import io.easybreezy.user.model.EmailTable
import io.easybreezy.user.model.Password
import io.easybreezy.user.model.User
import io.easybreezy.user.model.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class CreateDefaultUserCommand {

    companion object {
        private const val EMAIL = "admin@admin.my"

        @JvmStatic
        fun main(args: Array<String>) {
            val configProvider = SystemConfiguration
            val dataSource = HikariDataSource(configProvider)
            val database = Database.connect(dataSource)

            transaction {
                if (Users.select { Users.email[EmailTable.email] eq EMAIL}.count().compareTo(0) == 0) {
                    User.createAdmin(
                        Email.create(EMAIL),
                        Password.create("123")
                    )
                }
            }
        }
    }
}
