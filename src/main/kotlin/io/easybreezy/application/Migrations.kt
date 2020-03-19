package io.easybreezy.application

import org.flywaydb.core.Flyway

@Suppress("unused", "UnusedMainParameter")
class Migrations {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val provider = SystemConfiguration

            val flyway =
                Flyway.configure().locations("classpath:io/easybreezy/migrations")
                    .validateOnMigrate(true)
                    .outOfOrder(true)
                    .dataSource(
                        provider.get("bts.jdbc.url", String::class.java),
                        provider.get("bts.jdbc.user", String::class.java),
                        provider.get("bts.jdbc.password", String::class.java)
                    )
                    .baselineOnMigrate(true)
                    .load()

            flyway.migrate()
        }
    }
}