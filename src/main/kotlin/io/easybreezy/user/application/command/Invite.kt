package io.easybreezy.user.application.command

import kotlinx.serialization.Serializable

@Serializable
data class Invite(val email: String, val activities: Set<String>)
