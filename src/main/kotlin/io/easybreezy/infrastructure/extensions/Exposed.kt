package io.easybreezy.infrastructure.extensions

import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

fun EntityID<UUID>.toUUID(): UUID {
    return UUID.fromString(this.toString())
}