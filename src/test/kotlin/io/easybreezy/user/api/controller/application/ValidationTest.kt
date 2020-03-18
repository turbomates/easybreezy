package io.easybreezy.user.api.controller.application

import io.easybreezy.user.application.Contact
import io.easybreezy.user.application.UpdateContacts
import io.easybreezy.user.application.Validation
import io.easybreezy.user.infrastructure.UserRepository
import io.easybreezy.user.model.Contacts
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ValidationTest {

    private var validation = Validation(UserRepository())

    @Test fun `correct contacts data should pass validation`() {
        val command = UpdateContacts(listOf(
            Contact(Contacts.Type.SKYPE, "skype-n"),
            Contact(Contacts.Type.TELEGRAM, "telegram-n"),
            Contact(Contacts.Type.SLACK, "slack-n"),
            Contact(Contacts.Type.EMAIL, "email@example.com"),
            Contact(Contacts.Type.PHONE, "12-2343-24")
        ))

        Assertions.assertTrue(validation.onUpdateContacts(command).isEmpty())
    }

    @Test fun `invalid email address should not be accepted as contact`() {
        val command = UpdateContacts(listOf(Contact(Contacts.Type.EMAIL, "email-invalid")))
        Assertions.assertFalse(validation.onUpdateContacts(command).isEmpty())
    }
}
