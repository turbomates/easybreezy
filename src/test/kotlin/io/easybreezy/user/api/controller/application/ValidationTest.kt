package io.easybreezy.user.api.controller.application

import io.easybreezy.user.application.Contact
import io.easybreezy.user.application.UpdateContacts
import io.easybreezy.user.application.Validation
import io.easybreezy.user.infrastructure.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ValidationTest {

    private var validation = Validation(UserRepository())

    @Test fun `correct contacts data should pass validation`() {
        val command = UpdateContacts(listOf(
            Contact("SKYPE", "skype-n"),
            Contact("TELEGRAM", "telegram-n"),
            Contact("SLACK", "slack-n"),
            Contact("EMAIL", "email@example.com"),
            Contact("PHONE", "12-2343-24")
        ))

        Assertions.assertTrue(validation.onUpdateContacts(command).isEmpty())
    }

    @Test fun `unknown contact's type SKYPEEEE should raise error`() {
        val command = UpdateContacts(listOf(Contact("SKYPEEEE", "skype-n")))

        Assertions.assertFalse(validation.onUpdateContacts(command).isEmpty())
    }

    @Test fun `invalid email address should not be accepted as contact`() {
        val command = UpdateContacts(listOf(Contact("EMAIL", "email-invalid")))

        Assertions.assertFalse(validation.onUpdateContacts(command).isEmpty())
    }
}