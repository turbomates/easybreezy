package io.easybreezy.hr.application.profile.command

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
data class UpdateMessengers(val messengers: List<Messenger>) {
    @Transient
    lateinit var id: UUID
}

@Serializable
data class Messenger(val type: String, val username: String)
