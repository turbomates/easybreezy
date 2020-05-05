package io.easybreezy.user.application

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
data class UpdateActivities(val activities: Set<String>) {
    @Transient
    lateinit var userId: UUID
}
