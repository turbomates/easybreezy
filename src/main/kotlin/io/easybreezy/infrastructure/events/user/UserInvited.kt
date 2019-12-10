package io.easybreezy.infrastructure.events.user

import io.easybreezy.infrastructure.events.Event
import java.util.UUID

data class UserInvited(val id: UUID) : Event()
