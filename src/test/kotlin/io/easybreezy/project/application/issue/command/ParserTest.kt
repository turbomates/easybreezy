package io.easybreezy.project.application.issue.command

import io.easybreezy.project.application.issue.command.parser.Parser
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

    @Test fun `no title separator`() {
        val description = """
            Roles to Activities We upgrade (and obvious rename)
        """.trimIndent()

        val data = parser.parse(description)

        Assertions.assertEquals("Roles to A", data.title)
        Assertions.assertEquals("""
            ctivities We upgrade (and obvious rename)
        """.trimIndent(), data.description)
    }

    @Test fun `no title separator less 10 chars`() {
        val description = """
            Roles
        """.trimIndent()

        val data = parser.parse(description)

        Assertions.assertEquals("Roles", data.title)
        Assertions.assertEquals("", data.description)
    }

    @Test fun `extract category`() {
        val description = """
            Roles to Activities 
            We upgrade (and obvious rename) as <awesome category> and than other text>>
        """.trimIndent()
        val data = parser.parse(description)
        Assertions.assertEquals("awesome category", data.category)
    }

    @Test fun `extract persons`() {
        val description = """
            Roles to Activities 
            We upgrade (and obvious rename) @dima and ask @anna for details and @ other
        """.trimIndent()
        val data = parser.parse(description)
        Assertions.assertEquals("dima", data.assignee)
        Assertions.assertEquals(listOf("anna"), data.watchers)
    }

    @Test fun `extract priority`() {
        val description = """
            Roles to Activities 
            We upgrade (and obvious rename) high priority
        """.trimIndent()
        val data = parser.parse(description)
        Assertions.assertEquals("high", data.priority)
    }
}
