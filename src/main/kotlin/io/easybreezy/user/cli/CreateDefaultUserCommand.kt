package io.easybreezy.user.cli

import com.google.gson.JsonArray
import io.easybreezy.application.SystemConfiguration
import io.easybreezy.application.db.hikari.HikariDataSource
import io.easybreezy.application.db.jooq.jooqDSL
import io.easybreezy.tables.Users.USERS
import io.easybreezy.user.model.Password
import io.easybreezy.user.model.User
import java.util.UUID

class CreateDefaultUserCommand {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val configProvider = SystemConfiguration
            val dataSource = HikariDataSource(configProvider)

            dataSource.jooqDSL {
                if (!it.fetchExists(it.selectOne().from(USERS).where(USERS.EMAIL_ADDRESS.eq("admin@admin.my")))) {

                    val roles = JsonArray()
                    roles.add(User.Role.ADMIN.name)

                    it.insertInto(
                        USERS,
                        USERS.ID,
                        USERS.EMAIL_ADDRESS,
                        USERS.PASSWORD,
                        USERS.FIRST_NAME,
                        USERS.LAST_NAME,
                        USERS.ROLES
                    ).values(UUID.randomUUID(), "admin@admin.my", Password.hash("123"), "admin", "admin", roles)
                        .execute()
                }
            }
        }
    }
}
