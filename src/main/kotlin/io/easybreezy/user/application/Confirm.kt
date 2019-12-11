package io.easybreezy.user.application

data class Confirm(val token: String, val password: String, val firstName: String, val lastName: String)
