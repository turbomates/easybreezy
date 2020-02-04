package io.easybreezy.application

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.jdiazcano.cfg4k.loaders.EnvironmentConfigLoader
import com.jdiazcano.cfg4k.loaders.PropertyConfigLoader
import com.jdiazcano.cfg4k.loaders.SystemPropertyConfigLoader
import com.jdiazcano.cfg4k.providers.ConfigProvider
import com.jdiazcano.cfg4k.providers.OverrideConfigProvider
import com.jdiazcano.cfg4k.providers.ProxyConfigProvider
import com.jdiazcano.cfg4k.providers.getOrNull
import com.jdiazcano.cfg4k.sources.ConfigSource
import com.zaxxer.hikari.HikariConfig
import io.easybreezy.hr.HRModule
import io.easybreezy.infrastructure.event.EventSubscribers
import io.easybreezy.infrastructure.event.EventsDatabaseAccess
import io.easybreezy.infrastructure.event.SubscriberWorker
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.infrastructure.ktor.ErrorRenderer
import io.easybreezy.infrastructure.ktor.auth.Session
import io.easybreezy.infrastructure.ktor.auth.SessionSerializer
import io.easybreezy.project.ProjectModule
import io.easybreezy.user.UserModule
import io.easybreezy.user.api.interceptor.Auth
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.DataConversion
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Locations
import io.ktor.routing.routing
import io.ktor.serialization.DefaultJsonConfiguration
import io.ktor.serialization.serialization
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.sessions.directorySessionStorage
import io.ktor.util.DataConversionException
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.serializersModuleOf
import org.jetbrains.exposed.sql.Database
import org.slf4j.event.Level
import org.valiktor.ConstraintViolationException
import java.io.File
import java.io.InputStream
import java.util.UUID
import javax.sql.DataSource

suspend fun main() {
    val configProvider = SystemConfiguration
    val dataSource = HikariDataSource(configProvider)
    val database = Database.connect(dataSource)
    val eventSubscribers = EventSubscribers()
    val injector = Guice.createInjector(object : AbstractModule() {
        override fun configure() {
            bind(DataSource::class.java).toInstance(dataSource)
            bind(Database::class.java).toInstance(database)
            bind(TransactionManager::class.java).toInstance(TransactionManager(database))
            bind(EventSubscribers::class.java).toInstance(eventSubscribers)
        }
    })

    val subscriberWorker = SubscriberWorker(EventsDatabaseAccess(database), eventSubscribers)
    subscriberWorker.start(1)
    embeddedServer(Netty, port = 3000) {
        val application = this
        install(DefaultHeaders)
        install(Locations)
        install(CallLogging) {
            level = Level.DEBUG

            mdc("requestId") {
                UUID.randomUUID().toString()
            }
        }

        install(DataConversion) {
            convert<UUID> {
                decode { values, _ ->
                    values.singleOrNull()?.let { UUID.fromString(it) }
                }

                encode { value ->
                    when (value) {
                        null -> listOf()
                        is UUID -> listOf(value.toString())
                        else -> throw DataConversionException("Cannot convert $value as UUID")
                    }
                }
            }
        }

        install(Sessions) {
            cookie<Session>(
                "EASYBREEZYSESSION",
                directorySessionStorage(File(".sessions"), cached = false)

            ) {
                serializer = SessionSerializer()
                cookie.path = "/"
            }
        }

        install(StatusPages) {
            exception<ConstraintViolationException> { ErrorRenderer.render(call, it) }
            status(HttpStatusCode.Unauthorized) {
                ErrorRenderer.render(call, "You're not authorized", HttpStatusCode.Unauthorized)
            }
            exception<Exception> { ErrorRenderer.render(call, it) }
        }
        install(ContentNegotiation) {
            serialization(
                contentType = ContentType.Application.Json,
                json = Json(
                    configuration = DefaultJsonConfiguration.copy(
                        prettyPrint = true,
                        useArrayPolymorphism = true,
                        encodeDefaults = false
                    )
                )
            )
        }

        val ktorInjector = injector.createChildInjector(object : AbstractModule() {
            override fun configure() {
                bind(Application::class.java).toInstance(application)
            }
        })

        routing {
            injector.getInstance(Auth::class.java).intercept(this)
        }

        ktorInjector.createChildInjector(UserModule())
        ktorInjector.createChildInjector(ProjectModule())
        ktorInjector.createChildInjector(HRModule())
    }.start()
}

private fun buildConfiguration(): ConfigProvider {
    val resourceObject = object {}

    val propertyFiles = mutableListOf("/local.properties", "/default.properties", "/app.properties")

    val propsProviders = propertyFiles
        .asSequence()
        .map { resourceObject.javaClass.getResourceAsStream(it) }
        .filter { it != null }
        .map { PropertyConfigLoader(streamConfigSource(it)) }
        .map { ProxyConfigProvider(it) }
        .toList()
        .toTypedArray()

    return OverrideConfigProvider(
        ProxyConfigProvider(SystemPropertyConfigLoader()),
        ProxyConfigProvider(
            EnvironmentConfigLoader()
        ),
        *propsProviders
    )
}

private fun streamConfigSource(inputStream: InputStream): ConfigSource {
    return object : ConfigSource {
        override fun read(): InputStream {
            return inputStream
        }
    }
}

object SystemConfiguration : ConfigProvider by buildConfiguration()

internal val lookupDataSource: (configProvider: ConfigProvider) -> DataSource = { configProvider ->
    val hkConfig = HikariConfig().apply {
        connectionTestQuery = "select 1"
        jdbcUrl = configProvider.getOrNull("easybreezy.jdbc.url")
        username = configProvider.getOrNull("easybreezy.jdbc.user")
        password = configProvider.getOrNull("easybreezy.jdbc.password")
    }

    com.zaxxer.hikari.HikariDataSource(hkConfig)
}

abstract class HikariDataSourceBase(private val ds: DataSource) : DataSource by ds

class HikariDataSource(configProvider: ConfigProvider) : HikariDataSourceBase(lookupDataSource(configProvider))
