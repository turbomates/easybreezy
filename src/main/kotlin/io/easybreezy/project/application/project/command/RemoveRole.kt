package io.easybreezy.project.application.project.command

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
class RemoveRole {
    @Transient
    lateinit var roleId: UUID
    @Transient
    lateinit var project: String
}
