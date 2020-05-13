@file:UseSerializers(UUIDSerializer::class)
package io.easybreezy.hr.application.event.command

import io.easybreezy.hr.model.event.EventId
import io.easybreezy.hr.model.event.EmployeeId
import io.easybreezy.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers

@Serializable
data class Enter(val employeeId: EmployeeId) {
    @Transient
    lateinit var event: EventId
}
