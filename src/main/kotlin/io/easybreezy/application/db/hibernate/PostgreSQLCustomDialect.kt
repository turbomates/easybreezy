package io.easybreezy.application.db.hibernate

import io.easybreezy.application.db.hibernate.type.JsonBType
import java.sql.Types
import org.hibernate.dialect.PostgreSQL95Dialect

class PostgreSQLCustomDialect : PostgreSQL95Dialect() {
    init {
        this.registerHibernateType(
            Types.OTHER, JsonBType::class.java.name
        )
    }
}
