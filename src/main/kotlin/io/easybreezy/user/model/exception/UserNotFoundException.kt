package io.easybreezy.user.model.exception

import java.util.UUID

class UserNotFoundException(id: UUID) : Exception("User with id $id not found")
