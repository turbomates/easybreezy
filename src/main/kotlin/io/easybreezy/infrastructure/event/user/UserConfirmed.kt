package io.easybreezy.infrastructure.event.user

import io.easybreezy.infrastructure.event.Event
import io.easybreezy.infrastructure.event.project.project.TeamClosed
import java.util.UUID

data class UserConfirmed(val id: UUID) : Event {
    override val key
        get() = Companion

    companion object : Event.Key<UserConfirmed>
}
