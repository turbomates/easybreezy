package io.easybreezy.user.model.exception

class InvalidTokenException(token: String) : Exception("Invalid token - $token")
