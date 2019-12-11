package io.easybreezy.infrastructure.events.user

import io.easybreezy.infrastructure.events.Event
import java.util.UUID

data class UserConfirmed(val id: UUID) : Event()
