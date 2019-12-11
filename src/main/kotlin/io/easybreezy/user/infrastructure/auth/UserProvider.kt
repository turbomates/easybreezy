package io.easybreezy.user.infrastructure.auth

import com.google.inject.Inject
import io.easybreezy.infrastructure.ktor.auth.PrincipalProvider
import io.easybreezy.infrastructure.ktor.auth.UserPrincipal
import io.easybreezy.user.model_legacy.UserId
import io.ktor.auth.UserPasswordCredential
import io.ktor.auth.jwt.JWTCredential
import javax.sql.DataSource

class UserProvider @Inject constructor(private val dataSource: DataSource) : PrincipalProvider<UserPrincipal> {
    override fun load(credential: UserPasswordCredential, clientIp: String): UserPrincipal? {
        // val user = dataSource.jooqDSL {
        //     it.fetchOne(USERS,
        //         (USERS.EMAIL_ADDRESS.eq(credential.name)))
        // }
        //
        // if (user is UsersRecord) {
        //     val isSuccessful = Password.verifyPassword(credential.password, user.password)
        //     val roles = Gson().fromJson<Set<User.Role>>(user.roles, object : TypeToken<Set<String>>() {}.type)
        //
        //     if (isSuccessful) return UserPrincipal(user.id, roles)
        // }

        return null
    }

    override fun refresh(principal: UserPrincipal): UserPrincipal? {
        return UserPrincipal(principal.id, principal.roles)
    }

    fun load(credential: JWTCredential): UserPrincipal? {
        return UserPrincipal(UserId.fromString(credential.payload.subject), setOf())
    }
}
