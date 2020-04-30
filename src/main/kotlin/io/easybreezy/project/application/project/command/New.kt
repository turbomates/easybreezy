package io.easybreezy.project.application.project.command

import java.util.UUID

data class New(val author: UUID, val name: String, val description: String, val slug: String? = null)
