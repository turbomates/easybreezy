package io.easybreezy

import com.google.inject.AbstractModule
import com.google.inject.Guice
import io.easybreezy.hr.HRModule
import io.easybreezy.infrastructure.event.EventSubscribers
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.infrastructure.ktor.auth.Auth
import io.easybreezy.infrastructure.ktor.auth.Role
import io.easybreezy.infrastructure.ktor.auth.Session
import io.easybreezy.infrastructure.ktor.auth.SessionSerializer
import io.easybreezy.infrastructure.ktor.auth.UserPrincipal
import io.easybreezy.project.ProjectModule
import io.easybreezy.user.UserModule
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
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Locations
import io.ktor.response.respond
import io.ktor.serialization.DefaultJsonConfiguration
import io.ktor.serialization.json
import io.ktor.serialization.serialization
import io.ktor.server.testing.TestApplicationCall
import io.ktor.sessions.SessionStorageMemory
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import io.ktor.util.DataConversionException
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import java.util.UUID
import javax.sql.DataSource

fun Application.testApplication(userId: UUID, roles: Set<Role>, database: Database) {
    val dataSource = TestDataSource
    val eventSubscribers = EventSubscribers()
    val injector = Guice.createInjector(object : AbstractModule() {
        override fun configure() {
            bind(DataSource::class.java).toInstance(dataSource)
            bind(Database::class.java).toInstance(database)
            bind(TransactionManager::class.java).toInstance(TransactionManager(database))
            bind(EventSubscribers::class.java).toInstance(eventSubscribers)
        }
    })

    install(Sessions) {
        cookie<Session>("EASYBREEZYSESSION", SessionStorageMemory()) {
            serializer = SessionSerializer()
            cookie.path = "/"
        }
    }

    install(Locations)

    install(ContentNegotiation) {
        json(
            json = Json(
                DefaultJsonConfiguration.copy(
                    prettyPrint = true
                )
            ),
            contentType = ContentType.Application.Json
        )
    }

    install(StatusPages) {
        status(HttpStatusCode.Unauthorized) {
            call.respond(HttpStatusCode.Unauthorized, Error("You're not authorized"))
        }
        exception<Exception> {
            call.respond(HttpStatusCode.ServiceUnavailable, Error(it.localizedMessage))
        }
    }

    intercept(ApplicationCallPipeline.Call) {
        val session = Session()
        call.sessions.set(session.copy(principal = UserPrincipal(userId, roles)))
    }

    install(Authentication) {
        basic(Auth.UserFormAuth) {
            skipWhen {
                true
            }
        }
        session<Session>(Auth.UserSessionAuth) {
            validate {
                UserPrincipal(userId, roles)
            }
        }
        basic(Auth.JWTAuth) {
            skipWhen {
                true
            }
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

    val ktorInjector = injector.createChildInjector(object : AbstractModule() {
        override fun configure() {
            bind(Application::class.java).toInstance(this@testApplication)
        }
    })

    ktorInjector.createChildInjector(UserModule())
    ktorInjector.createChildInjector(ProjectModule())
    ktorInjector.createChildInjector(HRModule())
}

inline fun <R> withSwagger(receiver: TestApplicationCall, block: TestApplicationCall.() -> R): R {
    return with(receiver, { block() })
}
