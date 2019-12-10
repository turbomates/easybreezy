package io.easybreezy.application.db.hikari

import com.jdiazcano.cfg4k.providers.ConfigProvider
import com.jdiazcano.cfg4k.providers.getOrNull
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

internal val lookupDataSource: (configProvider: ConfigProvider) -> DataSource = { configProvider ->
    val hkConfig = HikariConfig().apply {
        connectionTestQuery = "select 1"
        jdbcUrl = configProvider.getOrNull("easybreezy.jdbc.url")
        username = configProvider.getOrNull("easybreezy.jdbc.user")
        password = configProvider.getOrNull("easybreezy.jdbc.password")
    }

    HikariDataSource(hkConfig)
}

abstract class HikariDataSourceBase(private val ds: DataSource) : DataSource by ds

class HikariDataSource(configProvider: ConfigProvider) : HikariDataSourceBase(lookupDataSource(configProvider))
