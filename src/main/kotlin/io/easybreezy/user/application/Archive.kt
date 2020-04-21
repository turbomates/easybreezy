package io.easybreezy.user.application

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*

@Serializable
data class Archive(val reason: String?) {
    @Transient
    lateinit var userId: UUID
}