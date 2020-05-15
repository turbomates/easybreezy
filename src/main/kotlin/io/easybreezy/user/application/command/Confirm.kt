package io.easybreezy.user.application.command

import kotlinx.serialization.Serializable

@Serializable
data class Confirm(val token: String, val password: String, val firstName: String, val lastName: String)
