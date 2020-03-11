package io.easybreezy.user.model

import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableClass
import io.easybreezy.infrastructure.exposed.dao.EmbeddableTable
import org.mindrot.jbcrypt.BCrypt

class Password private constructor() : Embeddable() {
    private var hashedPassword by PasswordTable.password

    companion object : EmbeddableClass<Password>(Password::class) {
        override fun createInstance(): Password {
            return Password()
        }

        fun create(plainPassword: String): Password {
            val password = Password()
            password.hashedPassword = hash(plainPassword)
            return password
        }

        fun verifyPassword(enteredPassword: String, password: String): Boolean {
            return BCrypt.checkpw(enteredPassword, password)
        }

        fun hash(plainPassword: String): String {
            return BCrypt.hashpw(plainPassword, BCrypt.gensalt())
        }
    }

    fun change(newPassword: String) {
        hashedPassword = hash(newPassword)
    }

    fun isValid(enteredPassword: String): Boolean {
        return BCrypt.checkpw(enteredPassword, hashedPassword)
    }
}

object PasswordTable : EmbeddableTable() {
    val password = varchar("password", 255).nullable()
}
