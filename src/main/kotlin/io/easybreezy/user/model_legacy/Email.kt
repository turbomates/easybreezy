package io.easybreezy.user.model_legacy

class Email(
    private val address: String
) {
    fun address(): String {
        return this.address
    }
}
