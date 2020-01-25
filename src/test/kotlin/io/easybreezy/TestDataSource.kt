package io.easybreezy

import com.jdiazcano.cfg4k.providers.getOrNull
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.easybreezy.application.SystemConfiguration
import java.sql.Connection
import javax.sql.DataSource

internal val lookupDataSource: () -> DataSource = {
    val hkConfig = HikariConfig().apply {
        connectionTestQuery = "select 1"
        jdbcUrl = SystemConfiguration.getOrNull("easybreezy.jdbc.url")
        username = SystemConfiguration.getOrNull("easybreezy.jdbc.user")
        password = SystemConfiguration.getOrNull("easybreezy.jdbc.password")
        isAutoCommit = false
    }

    HikariDataSource(hkConfig)
}

abstract class TestDataSourceBase(private val ds: DataSource) : DataSource by ds

object TestDataSource : TestDataSourceBase(lookupDataSource()) {
    override fun getConnection(): Connection {
        return TestConnection
    }
}
