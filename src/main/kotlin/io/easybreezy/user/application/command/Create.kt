package io.easybreezy.user.application.command

import kotlinx.serialization.Serializable

@Serializable
data class Create(
    val email: String,
    val firstName: String,
    val lastName: String,
    val activities: Set<String>
)
