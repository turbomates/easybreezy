package io.easybreezy.user.model

import org.mindrot.jbcrypt.BCrypt

class Password(plainPassword: String) {
    private var hashedPassword: String =
        hash(plainPassword)

    companion object {
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
