package io.easybreezy.user.cli

import io.easybreezy.application.HikariDataSource
import io.easybreezy.application.SystemConfiguration
import io.easybreezy.user.model.*
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
                if (Users.select { Users.email eq "admin@admin.my" }.count() == 0) {
                    User.createAdmin(
                        Email.create("admin@admin.my"),
                        Password.create("123")
                    )
                }

                //@todo remove
                 User.invite(Email.create("email@email.com"), mutableSetOf(Role.MEMBER))

            }
        }
    }
}
