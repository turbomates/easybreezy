package io.easybreezy.project.model.project

import java.util.UUID

class Status(private val name: String, private val priority: Short, private val resolved: Boolean = true) {
    private val id: UUID = UUID.randomUUID()
}
