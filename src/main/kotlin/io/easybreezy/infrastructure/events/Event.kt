package io.easybreezy.infrastructure.events

import java.util.UUID

abstract class Event {
    val sourceId: UUID = UUID.randomUUID()
}
