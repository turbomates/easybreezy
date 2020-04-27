package io.easybreezy.project.application.issue.command

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*

@Serializable
class Update(val text: String) {
    @Transient
    lateinit var id: UUID
}