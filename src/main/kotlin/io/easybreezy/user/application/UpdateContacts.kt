package io.easybreezy.user.application

import kotlinx.serialization.Serializable

@Serializable
data class UpdateContacts(val contacts: List<Contact>)

@Serializable
data class Contact(val type: String, val value: String)