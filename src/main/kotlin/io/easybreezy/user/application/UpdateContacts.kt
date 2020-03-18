package io.easybreezy.user.application

import io.easybreezy.user.model.Contacts
import kotlinx.serialization.Serializable

@Serializable
data class UpdateContacts(val contacts: List<Contact>)

@Serializable
data class Contact(
    val type: Contacts.Type,
    val value: String
)
