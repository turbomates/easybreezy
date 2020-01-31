package io.easybreezy

import com.google.inject.AbstractModule
import com.google.inject.Guice
import io.easybreezy.hr.HRModule
import io.easybreezy.infrastructure.event.EventSubscribers
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.infrastructure.ktor.ErrorRenderer
import io.easybreezy.infrastructure.ktor.auth.Auth
import io.easybreezy.infrastructure.ktor.auth.Session
import io.easybreezy.infrastructure.ktor.auth.SessionSerializer
import io.easybreezy.infrastructure.ktor.auth.UserPrincipal
import io.easybreezy.project.ProjectModule
import io.easybreezy.user.UserModule
import io.easybreezy.user.model.Role
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
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Locations
import io.ktor.serialization.DefaultJsonConfiguration
import io.ktor.serialization.serialization
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import io.ktor.sessions.*
import io.ktor.util.DataConversionException
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.Test
import org.valiktor.ConstraintViolationException
import java.sql.Connection
import java.util.*
import javax.sql.DataSource
import kotlin.test.assertEquals

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
        serialization(
            contentType = ContentType.Application.Json,
            json = Json(
                DefaultJsonConfiguration.copy(
                    prettyPrint = true
                )
            )
        )
    }

    install(StatusPages) {
        exception<ConstraintViolationException> { ErrorRenderer.render(call, it) }
        status(HttpStatusCode.Unauthorized) {
            ErrorRenderer.render(call, "You're not authorized", HttpStatusCode.Unauthorized)
        }
        exception<Exception> { ErrorRenderer.render(call, it) }
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