package io.easybreezy

import com.google.inject.AbstractModule
import com.google.inject.Guice
import io.easybreezy.hr.HRModule
import io.easybreezy.infrastructure.event.EventSubscribers
import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.infrastructure.ktor.Error
import io.easybreezy.infrastructure.ktor.RouteResponseInterceptor
import io.easybreezy.infrastructure.ktor.auth.Activity
import io.easybreezy.infrastructure.ktor.auth.Auth
import io.easybreezy.infrastructure.ktor.auth.Authorization
import io.easybreezy.infrastructure.ktor.auth.Session
import io.easybreezy.infrastructure.ktor.auth.SessionSerializer
import io.easybreezy.infrastructure.ktor.auth.UserPrincipal
import io.easybreezy.infrastructure.ktor.openapi.DescriptionBuilder
import io.easybreezy.infrastructure.serialization.LocalDateSerializer
import io.easybreezy.infrastructure.serialization.LocalDateTimeSerializer
import io.easybreezy.integration.openapi.ktor.OpenApi
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
import io.ktor.features.DoubleReceive
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Locations
import io.ktor.response.respond
import io.ktor.routing.routing
import io.ktor.serialization.DefaultJsonConfiguration
import io.ktor.serialization.json
import io.ktor.server.testing.TestApplicationCall
import io.ktor.sessions.SessionStorageMemory
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import io.ktor.util.DataConversionException
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.serializersModuleOf
import org.jetbrains.exposed.sql.Database
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.sql.DataSource

fun Application.testApplication(userId: UUID = UUID.randomUUID(), database: Database = testDatabase) {
    val dataSource = TestDataSource
    val eventSubscribers = EventSubscribers()
    val activities = Activity.values().toSet()
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
    install(OpenApi) {
        responseBuilder = { type -> DescriptionBuilder(type).buildResponseMap() }
        typeBuilder = { type -> DescriptionBuilder(type).buildType() }
        path = "/api/openapi.json"
    }
    install(ContentNegotiation) {
        json(
            contentType = ContentType.Application.Json,
            json = Json(
                configuration = DefaultJsonConfiguration.copy(
                    prettyPrint = true,
                    useArrayPolymorphism = true,
                    encodeDefaults = false
                ),
                context = serializersModuleOf(
                    mapOf(
                        LocalDateTime::class to LocalDateTimeSerializer,
                        LocalDate::class to LocalDateSerializer
                    )
                )
            )
        )
    }

    install(StatusPages) {
        status(HttpStatusCode.Unauthorized) {
            call.respond(HttpStatusCode.Unauthorized, Error("You're not authorized"))
        }
        exception<NoSuchElementException> {
            call.respond(HttpStatusCode.NotFound, Error(it.localizedMessage))
        }
        exception<Exception> {
            call.respond(HttpStatusCode.ServiceUnavailable, Error(it.localizedMessage))
        }
    }

    intercept(ApplicationCallPipeline.Call) {
        val session = Session()
        call.sessions.set(session.copy(principal = UserPrincipal(userId, activities)))
    }

    install(Authentication) {
        basic(Auth.UserFormAuth) {
            skipWhen {
                true
            }
        }
        session<Session>(Auth.UserSessionAuth) {
            validate {
                UserPrincipal(userId, activities)
            }
        }
        basic(Auth.JWTAuth) {
            skipWhen {
                true
            }
        }
    }
    install(Authorization) {
        validate { permissions ->
            for (permission in permissions) {
                if (activities.contains(permission)) {
                    return@validate true
                }
            }
            false
        }
        challenge {
            context.response.call.respond(HttpStatusCode.Forbidden, "Unauthorized for this action")
        }
    }
    install(DoubleReceive) {
        receiveEntireContent = true
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
    routing {
        injector.getInstance(RouteResponseInterceptor::class.java).intercept(this)
    }
    ktorInjector.createChildInjector(UserModule())
    ktorInjector.createChildInjector(ProjectModule())
    ktorInjector.createChildInjector(HRModule())
}

inline fun <R> withSwagger(receiver: TestApplicationCall, block: TestApplicationCall.() -> R): R {
    return with(receiver, { block() })
}

val testDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
val testDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
