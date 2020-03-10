package io.easybreezy.infrastructure.exposed

import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.UUID

fun EntityID<UUID>.toUUID(): UUID {
    return UUID.fromString(this.toString())
}

fun Date.toLocalDate(): LocalDate {
    return this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
}
