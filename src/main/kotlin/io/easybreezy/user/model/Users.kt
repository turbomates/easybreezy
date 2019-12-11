package io.easybreezy.user.model

import org.jetbrains.exposed.dao.UUIDTable

object Users : UUIDTable() {
    val password = varchar("password", 255).nullable()
    val email = varchar("email_address", 255)
    val firstName = varchar("first_name", 255)
    val lastName = varchar("last_name", 255)
    val token = varchar("token", 255).nullable()
}
