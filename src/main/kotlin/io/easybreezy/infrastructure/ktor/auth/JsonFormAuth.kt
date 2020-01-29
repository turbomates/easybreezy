package io.easybreezy.infrastructure.ktor.auth

import io.easybreezy.infrastructure.ktor.ErrorRenderer
import io.ktor.application.call
import io.ktor.auth.Authentication
import io.ktor.auth.AuthenticationFailedCause
import io.ktor.auth.AuthenticationPipeline
import io.ktor.auth.AuthenticationProvider
import io.ktor.auth.UserPasswordCredential
import io.ktor.features.origin
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.sessions.get
import io.ktor.sessions.sessions

/**
 * Represents a form-based authentication provider
 * @param name is the name of the provider, or `null` for a default provider
 */
class JsonFormAuthenticationProvider<T : Principal> internal constructor(config: Configuration) :
    AuthenticationProvider(config) {

    class Configuration internal constructor(name: String?) : AuthenticationProvider.Configuration(name)

    /**
     * POST parameter to fetch for a client name
     */
    var emailParamName: String = "email"

    /**
     * POST parameter to fetch for a client password
     */
    var passwordParamName: String = "password"

    lateinit var provider: PrincipalProvider<T>
}

/**
 * Installs Form Authentication mechanism
 */
fun <T : Principal> Authentication.Configuration.jsonForm(
    name: String? = null,
    configure: JsonFormAuthenticationProvider<T>.() -> Unit
) {

    val configuration = JsonFormAuthenticationProvider.Configuration(name)
    configuration.skipWhen { call ->
        val session = call.sessions.get<Session>()

        session?.principal != null
    }

    val provider = JsonFormAuthenticationProvider<T>(configuration).apply(configure)
    val emailParamName = provider.emailParamName
    val passwordParamName = provider.passwordParamName
    val authProvider = provider.provider
    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->
        val postParameters = call.receiveOrNull<Map<String, String>>()
        val email = postParameters?.get(emailParamName)
        val password = postParameters?.get(passwordParamName)
        val credentials = if (email != null && password != null) UserPasswordCredential(email, password) else null

        val principal = credentials?.let { authProvider.load(it, call.request.origin.remoteHost) }

        if (principal != null) {
            context.principal(principal)
        } else {
            val cause =
                if (credentials == null) AuthenticationFailedCause.NoCredentials else AuthenticationFailedCause.InvalidCredentials
            context.challenge(formAuthenticationChallengeKey, cause) {
                ErrorRenderer.render(call, "Bad credentials", HttpStatusCode.Unauthorized)
                it.complete()
            }
        }
    }
    register(provider)
}

private val formAuthenticationChallengeKey: Any = "JsonFormAuth"
