package io.easybreezy.user

import com.google.inject.AbstractModule
import com.google.inject.Guice
import io.easybreezy.application.db.test.TestDataSource
import io.easybreezy.application.db.test.TestHibernate
import io.easybreezy.infrastructure.gson.AbstractTypeAdapter
import io.easybreezy.infrastructure.ktor.ErrorRenderer
import io.easybreezy.infrastructure.ktor.auth.Auth
import io.easybreezy.infrastructure.ktor.auth.GsonSessionSerializer
import io.easybreezy.infrastructure.ktor.auth.Principal
import io.easybreezy.infrastructure.ktor.auth.Session
import io.easybreezy.infrastructure.ktor.auth.UserPrincipal
import io.easybreezy.user.model_legacy.User
import io.ktor.application.Application
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.basic
import io.ktor.auth.session
import io.ktor.features.ContentNegotiation
import io.ktor.features.DataConversion
import io.ktor.features.StatusPages
import io.ktor.gson.GsonConverter
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Locations
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.createTestEnvironment
import io.ktor.sessions.SessionStorageMemory
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import io.ktor.util.DataConversionException
import org.hibernate.SessionFactory
import org.valiktor.ConstraintViolationException
import java.util.UUID
import javax.sql.DataSource

class TestEngine {

    companion object {
        fun create(userId: UUID? = null, roles: Set<User.Role> = setOf()): TestApplicationEngine {
            val engine = TestApplicationEngine(createTestEnvironment())
            engine.start(wait = false)

            engine.application.install(Sessions) {
                cookie<Session>("EASYBREEZYSESSION", SessionStorageMemory()) {
                    serializer = GsonSessionSerializer(Session::class.java) {
                        registerTypeAdapter(Principal::class.java, AbstractTypeAdapter<Principal>())
                    }
                    cookie.path = "/"
                }
            }

            engine.application.install(Locations)

            engine.application.install(ContentNegotiation) {
                register(ContentType.Application.Json, GsonConverter())
            }

            val dataSource = TestDataSource
            val sessionFactory = TestHibernate(dataSource)

            val injector = Guice.createInjector(object : AbstractModule() {
                override fun configure() {
                    bind(DataSource::class.java).toInstance(dataSource)
                    bind(SessionFactory::class.java).toInstance(sessionFactory)
                }
            })

            engine.application.install(StatusPages) {
                exception<ConstraintViolationException> { ErrorRenderer.render(call, it) }
                status(HttpStatusCode.Unauthorized) {
                    ErrorRenderer.render(call, "You're not authorized", HttpStatusCode.Unauthorized)
                }
                exception<Exception> { ErrorRenderer.render(call, it) }
            }

            engine.application.intercept(ApplicationCallPipeline.Call) {
                val session = Session()
                call.sessions.set(session.copy(principal = UserPrincipal(userId!!, roles)))
            }

            engine.application.install(Authentication) {
                basic(Auth.UserFormAuth) {
                    skipWhen {
                        true
                    }
                }
                session<Session>(Auth.UserSessionAuth) {
                    validate {
                        UserPrincipal(userId!!, roles)
                    }
                }
                basic(Auth.JWTAuth) {
                    skipWhen {
                        true
                    }
                }
            }

            engine.application.install(DataConversion) {
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

            val ktorInjector = injector.createChildInjector(object : AbstractModule() {
                override fun configure() {
                    bind(Application::class.java).toInstance(engine.application)
                }
            })

            ktorInjector.createChildInjector(UserModule())

            return engine
        }
    }
}
