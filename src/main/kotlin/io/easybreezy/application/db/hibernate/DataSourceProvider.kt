package io.easybreezy.application.db.hibernate

import com.zaxxer.hikari.hibernate.HikariConnectionProvider
import java.io.Closeable
import java.sql.Connection
import javax.sql.DataSource
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider
import org.hibernate.service.UnknownUnwrapTypeException
import org.hibernate.service.spi.Stoppable

class DataSourceProvider(private val dataSource: DataSource) : ConnectionProvider, Stoppable {

    override fun isUnwrappableAs(unwrapType: Class<*>?): Boolean {
        return ConnectionProvider::class.java == unwrapType || HikariConnectionProvider::class.java.isAssignableFrom(
            unwrapType
        )
    }

    override fun closeConnection(conn: Connection?) {
        conn?.close()
    }

    override fun supportsAggressiveRelease(): Boolean {
        return false
    }

    override fun <T : Any?> unwrap(unwrapType: Class<T>?): T {
        if (ConnectionProvider::class.java == unwrapType ||
            HikariConnectionProvider::class.java.isAssignableFrom(unwrapType)
        ) {
            @Suppress("UNCHECKED_CAST")
            return this as T
        } else if (DataSource::class.java.isAssignableFrom(unwrapType)) {
            @Suppress("UNCHECKED_CAST")
            return this.dataSource as T
        } else {
            throw UnknownUnwrapTypeException(unwrapType)
        }
    }

    override fun getConnection(): Connection {
        return dataSource.connection
    }

    override fun stop() {
        if (dataSource is Closeable) dataSource.close()
    }
}
