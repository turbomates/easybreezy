package io.easybreezy.user.application.command

import io.easybreezy.user.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UpdateContacts(val contacts: List<User.Contact>)
