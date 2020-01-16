package io.easybreezy.infrastructure.event.user

import io.easybreezy.infrastructure.event.Event
import java.util.UUID

data class UserInvited(val id: UUID) : Event {
    override val key
        get() = Companion

    companion object : Event.Key<UserInvited>
}
