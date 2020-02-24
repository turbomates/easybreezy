package io.easybreezy.infrastructure.ktor.auth

import kotlinx.serialization.Serializable

@Serializable
enum class Role {
    ADMIN, MEMBER
}