package io.easybreezy.hr.application.event.command

import io.easybreezy.hr.model.event.EventId
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ChangeConditions(
    val isPrivate: Boolean? = null,
    val isFreeEntry: Boolean? = null,
    val isRepeatable: Boolean? = null
) {
    @Transient
    lateinit var event: EventId
}
