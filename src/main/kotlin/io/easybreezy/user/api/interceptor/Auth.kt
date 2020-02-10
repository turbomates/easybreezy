package io.easybreezy.user.api.interceptor

import com.google.inject.Inject
import io.easybreezy.infrastructure.structure.Either
import io.easybreezy.infrastructure.ktor.Interceptor
import io.easybreezy.infrastructure.ktor.Response
import io.easybreezy.infrastructure.ktor.auth.Auth
import io.easybreezy.infrastructure.ktor.auth.Session
import io.easybreezy.infrastructure.ktor.auth.UserPrincipal
import io.easybreezy.infrastructure.ktor.auth.jsonForm
import io.easybreezy.infrastructure.ktor.get
import io.easybreezy.infrastructure.ktor.post
import io.easybreezy.user.infrastructure.auth.UserProvider
import io.easybreezy.user.infrastructure.security.JWT
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.JWTCredential
import io.ktor.auth.jwt.jwt
import io.ktor.auth.principal
import io.ktor.auth.session
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.application
import io.ktor.routing.route
import io.ktor.sessions.clear
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set

class Auth @Inject constructor(private val userProvider: UserProvider) : Interceptor() {
    override fun intercept(route: Route) {
        val application = route.application

        application.install(Authentication) {
            jsonForm<UserPrincipal>(Auth.UserFormAuth) {
                provider = userProvider
            }
            session<Session>(Auth.UserSessionAuth) {
                validate {
                    it.principal?.let { principal -> userProvider.refresh(principal) }
                }
                challenge {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }
            jwt(Auth.JWTAuth) {
                verifier(JWT.verifier)
                validate {
                    if (it.payload.audience.contains(JWT.audience)) userProvider.load(JWTCredential(it.payload)) else null
                }
            }
        }
        login(route)
    }

    private fun login(route: Route) {

        route.route("/api/login") {
            authenticate(Auth.UserFormAuth) {
                post<Response.Either<Response.Ok, Response.Data<String>>> {
                    val principal: UserPrincipal? = call.principal()
                    principal ?: return@post Response.Either(Either.Left(Response.Ok))
                    val session = call.sessions.get<Session>() ?: Session()
                    call.sessions.set(session.copy(principal = principal))
                    Response.Either(Either.Right(Response.Data(JWT.create(principal.id))))
                }
            }
        }

        route.get<Response.Ok>("/api/logout") {
            call.sessions.clear<Session>()
            Response.Ok
        }
    }
}
