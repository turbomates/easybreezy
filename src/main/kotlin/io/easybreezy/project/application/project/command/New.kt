package io.easybreezy.project.application.project.command

import kotlinx.serialization.Serializable

@Serializable
data class New(val name: String, val description: String)
