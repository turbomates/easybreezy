package io.easybreezy.user.model

class Email(
    private val address: String
) {
    fun address(): String {
        return this.address
    }
}
