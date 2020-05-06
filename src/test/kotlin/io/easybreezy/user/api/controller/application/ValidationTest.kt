package io.easybreezy.user.api.controller.application

import io.easybreezy.infrastructure.exposed.TransactionManager
import io.easybreezy.testDatabase
import io.easybreezy.user.application.command.UpdateContacts
import io.easybreezy.user.application.command.Validation
import io.easybreezy.user.infrastructure.UserRepository
import io.easybreezy.user.model.Contacts
import io.easybreezy.user.model.User
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ValidationTest {
    private var validation = Validation(
        TransactionManager(testDatabase),
        UserRepository()
    )

    @Test fun `correct contacts data should pass validation`() {
        val command = UpdateContacts(
            listOf(
                User.Contact(Contacts.Type.SKYPE, "skype-n"),
                User.Contact(Contacts.Type.TELEGRAM, "telegram-n"),
                User.Contact(Contacts.Type.SLACK, "slack-n"),
                User.Contact(
                    Contacts.Type.EMAIL,
                    "email@example.com"
                ),
                User.Contact(Contacts.Type.PHONE, "12-2343-24")
            )
        )

        Assertions.assertTrue(validation.onUpdateContacts(command).isEmpty())
    }

    @Test fun `invalid email address should not be accepted as contact`() {
        val command = UpdateContacts(
            listOf(
                User.Contact(
                    Contacts.Type.EMAIL,
                    "email-invalid"
                )
            )
        )
        Assertions.assertFalse(validation.onUpdateContacts(command).isEmpty())
    }
}
