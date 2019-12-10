package io.easybreezy.infrastructure.ktor

import io.ktor.application.install
import io.ktor.routing.Route
import org.hibernate.SessionFactory
import javax.inject.Inject
import javax.sql.DataSource
import io.easybreezy.infrastructure.ktor.features.HibernateSession

class HibernateSession @Inject constructor(private val hibernateFactory: SessionFactory, private val localDataSource: DataSource) : Interceptor() {
    override fun intercept(route: Route) {
        route.install(HibernateSession) {
            factory = hibernateFactory
            dataSource = localDataSource
        }
    }
}
