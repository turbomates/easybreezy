package io.easybreezy.project.application.project.command

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
class RemoveCategory {
    @Transient
    lateinit var categoryId: UUID
    @Transient
    lateinit var project: String
}
