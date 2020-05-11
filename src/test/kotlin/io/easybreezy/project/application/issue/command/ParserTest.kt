package io.easybreezy.project.application.issue.command

import io.ktor.client.tests.utils.assertFailsWith
import io.ktor.util.InternalAPI
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ParserTest {

    private val parser = Parser()

    @Test fun `title new line`() {
        val description = """
            Roles to Activities
            We upgrade (and obvious rename) roles to
            activities and add activities update.
        """.trimIndent()

        val data = parser.parse(description)

        Assertions.assertEquals("Roles to Activities", data.title)
        Assertions.assertEquals("""
            We upgrade (and obvious rename) roles to
            activities and add activities update.
        """.trimIndent(), data.description)
    }

    @Test fun `title dot`() {
        val description = """
            Roles to Activities.We upgrade (and obvious rename)
        """.trimIndent()

        val data = parser.parse(description)

        Assertions.assertEquals("Roles to Activities.", data.title)
        Assertions.assertEquals("""
            We upgrade (and obvious rename)
        """.trimIndent(), data.description)
    }

    @InternalAPI
    @Test fun `no title separator`() {
        val description = """
            Roles to Activities We upgrade (and obvious rename)
        """.trimIndent()

        assertFailsWith<IssueParserException> { parser.parse(description) }
    }
}