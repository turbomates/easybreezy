package io.easybreezy.user.infrastructure.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.easybreezy.user.model.UserId
import java.util.Date

object JWT {
    const val audience = "prudenta"
    private const val ttl: Int = 3600000 * 10 // 10 hours
    private val algorithm: Algorithm = Algorithm.HMAC256("secret")

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .build()

    fun create(id: UserId, ttl: Int = this.ttl, once: Boolean = false): String = JWT
        .create()
        .withSubject(id.toString())
        .withExpiresAt(Date(System.currentTimeMillis() + ttl))
        .withIssuedAt(Date(System.currentTimeMillis()))
        .withClaim("rnd", (0..100).random())
        .withClaim("once", once)
        .withAudience(audience)
        .sign(algorithm)
}
