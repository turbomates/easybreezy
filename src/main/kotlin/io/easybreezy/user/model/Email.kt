package io.easybreezy.user.model

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class Email(
    @Column(name = "email_address")
    private val address: String
) {
    fun address(): String {
        return this.address
    }
}
