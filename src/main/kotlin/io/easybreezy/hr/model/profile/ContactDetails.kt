package io.easybreezy.hr.model.profile

import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableClass
import org.jetbrains.exposed.sql.ResultRow

class ContactDetails private constructor() : Embeddable() {
    private var phones by Profiles.phones
    private var emails by Profiles.emails

    companion object : EmbeddableClass<ContactDetails>(ContactDetails::class) {
        override fun createInstance(resultRow: ResultRow?): ContactDetails {
            return ContactDetails()
        }

        fun create(phones: Set<Profiles.Phone>, emails: Set<Profiles.Email>): ContactDetails {
            val details = ContactDetails()
            details.phones = phones
            details.emails = emails
            return details
        }
    }
}
