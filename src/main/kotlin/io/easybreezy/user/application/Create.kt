package io.easybreezy.user.application

import kotlinx.serialization.Serializable

@Serializable
data class Create(
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: Set<String>
)