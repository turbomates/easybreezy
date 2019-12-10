package io.easybreezy.infrastructure.ktor.auth

object Auth {
    const val UserFormAuth = "UserFormAuth"
    const val UserSessionAuth = "UserSessionAuth"
    const val JWTAuth = "JWTAuth"
    val user = arrayOf(UserSessionAuth, JWTAuth)
}
