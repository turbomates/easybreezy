package io.easybreezy.application

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.jdiazcano.cfg4k.loaders.EnvironmentConfigLoader
import com.jdiazcano.cfg4k.loaders.PropertyConfigLoader
import com.jdiazcano.cfg4k.loaders.SystemPropertyConfigLoader
import com.jdiazcano.cfg4k.providers.ConfigProvider
import com.jdiazcano.cfg4k.providers.OverrideConfigProvider
import com.jdiazcano.cfg4k.providers.ProxyConfigProvider
import com.jdiazcano.cfg4k.sources.ConfigSource
import io.easybreezy.application.db.hibernate.DataSourceProvider
import io.easybreezy.application.db.hibernate.PostgreSQLCustomDialect
import io.easybreezy.application.db.hikari.HikariDataSource
import io.easybreezy.infrastructure.gson.AbstractTypeAdapter
import io.easybreezy.infrastructure.ktor.ErrorRenderer
import io.easybreezy.infrastructure.ktor.auth.GsonSessionSerializer
import io.easybreezy.infrastructure.ktor.auth.Session
import io.easybreezy.user.UserModule
import io.easybreezy.user.api.interceptor.Auth
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Principal
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DataConversion
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.gson.GsonConverter
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Locations
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.sessions.directorySessionStorage
import io.ktor.util.DataConversionException
import org.hibernate.SessionFactory
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.cfgxml.internal.ConfigLoader
import org.hibernate.boot.cfgxml.spi.LoadedConfig
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService
import org.slf4j.event.Level
import org.valiktor.ConstraintViolationException
import java.io.File
import java.io.InputStream
import java.util.UUID
import javax.sql.DataSource

fun main() {
    val configProvider = SystemConfiguration
    val dataSource = HikariDataSource(SystemConfiguration)
    val sessionFactory = configureHibernate(dataSource)

    val injector = Guice.createInjector(object : AbstractModule() {
        override fun configure() {
            bind(DataSource::class.java).toInstance(dataSource)
            bind(SessionFactory::class.java).toInstance(sessionFactory)
        }
    })

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
                serializer = GsonSessionSerializer(Session::class.java) {
                    registerTypeAdapter(Principal::class.java, AbstractTypeAdapter<Principal>())
                }
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
            register(ContentType.Application.Json, GsonConverter())
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
    }.start()
}

private fun configureHibernate(dataSource: DataSource): SessionFactory {
    val provider = DataSourceProvider(dataSource)
    val loadedConfig = LoadedConfig.baseline()
    val serviceRegistry = BootstrapServiceRegistryBuilder().enableAutoClose().build()
    val configLoader = ConfigLoader(serviceRegistry)

    // toDO - подумать над расположением конфигов хибернейта
    val hibernateResources =
        serviceRegistry.getService(ClassLoaderService::class.java).locateResources("hibernate.cfg.xml")

    for (resourceURL in hibernateResources) {
        loadedConfig.merge(configLoader.loadConfigXmlUrl(resourceURL))
    }
    val registry = StandardServiceRegistryBuilder(serviceRegistry, loadedConfig)
        .applySetting("hibernate.connection.provider_class", provider)
        .applySetting("hibernate.current_session_context_class", "thread")
        .applySetting("org.hibernate.flushMode ", "COMMIT")
        .applySetting("hibernate.dialect", PostgreSQLCustomDialect::class.java.name)
        .applySetting("show_sql", true)
        .build()
    try {
        return MetadataSources(registry).buildMetadata().buildSessionFactory()
    } catch (e: Exception) {
        // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
        // so destroy it manually.
        StandardServiceRegistryBuilder.destroy(registry)

        throw ExceptionInInitializerError("Initial SessionFactory failed $e")
    }
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
