package io.easybreezy.user.model

import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableClass
import io.easybreezy.infrastructure.exposed.dao.EmbeddableTable
import java.util.regex.Pattern

class Email private constructor() : Embeddable() {
    var address by EmailTable.email
        private set

    companion object : EmbeddableClass<Email>(Email::class) {
        override fun createInstance(): Email {
            return Email()
        }

        fun create(address: String): Email {
            require(isValid(address)) { "Email $address is wrong" }

            val email = Email()
            email.address = address
            return email
        }

        private fun isValid(email: String): Boolean {
            return Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@" +
                        "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" +
                        "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." +
                        "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" +
                        "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|" +
                        "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
            ).matcher(email).matches()
        }
    }
}

object EmailTable : EmbeddableTable() {
    val email = varchar("email_address", 255) // .uniqueIndex()
}
