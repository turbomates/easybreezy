package io.easybreezy.user.infrastructure.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date
import java.util.UUID

object JWT {
    const val audience = "easybreezy"
    private const val ttl: Long = 2592000000 * 3 // 3 months
    private val algorithm: Algorithm = Algorithm.HMAC256("secret")

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .build()

    fun create(id: UUID, ttl: Long = this.ttl, once: Boolean = false): String = JWT
        .create()
        .withSubject(id.toString())
        .withExpiresAt(Date(System.currentTimeMillis() + ttl))
        .withIssuedAt(Date(System.currentTimeMillis()))
        .withClaim("rnd", (0..100).random())
        .withClaim("once", once)
        .withAudience(audience)
        .sign(algorithm)
}
