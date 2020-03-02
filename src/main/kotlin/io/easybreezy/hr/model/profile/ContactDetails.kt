package io.easybreezy.hr.model.profile

import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableClass
import io.easybreezy.infrastructure.exposed.dao.EmbeddableTable
import io.easybreezy.infrastructure.exposed.type.jsonb
import kotlinx.serialization.Serializable
import kotlinx.serialization.set

class ContactDetails private constructor() : Embeddable() {
    private var phones by ContactDetailsTable.phones
    private var emails by ContactDetailsTable.emails

    companion object : EmbeddableClass<ContactDetails>(ContactDetails::class) {
        override fun createInstance(): ContactDetails {
            return ContactDetails()
        }

        fun create(phones: Set<Phone>, emails: Set<Email>): ContactDetails {
            val details = ContactDetails()
            details.phones = phones
            details.emails = emails
            return details
        }
    }
}

object ContactDetailsTable : EmbeddableTable() {
    val phones = jsonb("phones", Phone.serializer().set).nullable()
    val emails = jsonb("emails", Email.serializer().set).nullable()

}

@Serializable
class Phone(val number: String)

@Serializable
class Email(val address: String)
