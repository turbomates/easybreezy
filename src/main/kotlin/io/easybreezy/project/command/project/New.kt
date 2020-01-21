package io.easybreezy.project.command.project

import kotlinx.serialization.Serializable

@Serializable
data class New(val name: String, val description: String)