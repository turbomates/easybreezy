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
        @JvmStatic
        fun main(args: Array<String>) {
            val configProvider = SystemConfiguration
            val dataSource = HikariDataSource(configProvider)
            Database.connect(dataSource)

            transaction {
                if (Users.select { Users.email[EmailTable.email] eq "admin@admin.my" }.count().compareTo(0) == 0) {
                    User.createAdmin(
                        Email.create("admin@admin.my"),
                        Password.create("123")
                    )
                }
            }
        }
    }
}
