package io.easybreezy.project.application.issue.command

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*

@Serializable
data class New(val content: String) {
    @Transient
    lateinit var project: String
    @Transient
    lateinit var author: UUID
}