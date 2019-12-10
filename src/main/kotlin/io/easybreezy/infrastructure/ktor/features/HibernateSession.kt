package io.easybreezy.infrastructure.ktor.features

import io.easybreezy.infrastructure.hibernate.HibernateInterceptor
import io.ktor.application.ApplicationCall
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.application.call
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.coroutineScope
import org.hibernate.Session
import org.hibernate.SessionFactory
import javax.sql.DataSource

val HibernateSessionKey = AttributeKey<Session>("hibernateSessionAttributeKey")

class HibernateSession(configuration: Configuration) {
    private val factory: SessionFactory = configuration.factory
    private val dataSource: DataSource = configuration.dataSource

    class Configuration {
        lateinit var factory: SessionFactory
        lateinit var dataSource: DataSource
    }

    private suspend fun intercept(context: PipelineContext<Unit, ApplicationCall>) {
        dataSource.connection.use { connection ->
            val builder = factory
                .withOptions()
                .connection(connection)
                .interceptor(HibernateInterceptor(connection))
            val session = builder.openSession()
            session.use {
                context.call.attributes.put(HibernateSessionKey, it)
                coroutineScope {
                    context.proceed()
                }
            }
        }
    }

    companion object Feature : ApplicationFeature<ApplicationCallPipeline, Configuration, HibernateSession> {
        override val key = AttributeKey<HibernateSession>("HibernateScope")
        override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): HibernateSession {
            val configuration = Configuration().apply(configure)
            val feature = HibernateSession(configuration)

            pipeline.intercept(ApplicationCallPipeline.Monitoring) {
                feature.intercept(this)
            }

            return feature
        }
    }
}
