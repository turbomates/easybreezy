package io.easybreezy.user.application.command

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
data class Archive(val reason: String?) {
    @Transient
    lateinit var userId: UUID
}
