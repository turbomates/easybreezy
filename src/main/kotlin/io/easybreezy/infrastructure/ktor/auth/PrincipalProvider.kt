package io.easybreezy.infrastructure.ktor.auth

import io.ktor.auth.Principal
import io.ktor.auth.UserPasswordCredential

interface PrincipalProvider<T : Principal> {
    suspend fun load(credential: UserPasswordCredential, clientIp: String): T?
    fun refresh(principal: T): T?
}
