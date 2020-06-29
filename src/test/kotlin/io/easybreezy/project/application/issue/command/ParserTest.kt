package io.easybreezy.project.application.issue.command

import io.easybreezy.project.application.issue.command.language.Parser
import io.easybreezy.project.application.issue.command.language.LanguageTranslator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ParserTest {
    private val parser = Parser(LanguageTranslator())

    @Test fun `title new line`() {
        val description = """
            Roles to Activities
            We upgrade (and obvious rename) roles to
            activities and add activities update.
        """.trimIndent()

        val data = parser.parse(description)

        Assertions.assertEquals("Roles to Activities", data.titleDescription.title)
        Assertions.assertEquals("""
            We upgrade (and obvious rename) roles to
            activities and add activities update.
        """.trimIndent(), data.titleDescription.description)
    }

    @Test fun `title dot`() {
        val description = """
            Roles to Activities.We upgrade (and obvious rename)
        """.trimIndent()

        val data = parser.parse(description)

        Assertions.assertEquals("Roles to Activities.", data.titleDescription.title)
        Assertions.assertEquals("""
            We upgrade (and obvious rename)
        """.trimIndent(), data.titleDescription.description)
    }

    @Test fun `no title separator`() {
        val description = """
            Roles to Activities We upgrade (and obvious rename)
        """.trimIndent()

        val data = parser.parse(description)

        Assertions.assertEquals("Roles to A", data.titleDescription.title)
        Assertions.assertEquals("""
            Roles to Activities We upgrade (and obvious rename)
        """.trimIndent(), data.titleDescription.description)
    }

    @Test fun `no title separator less 10 chars`() {
        val description = """
            Roles
        """.trimIndent()

        val data = parser.parse(description)

        Assertions.assertEquals("Roles", data.titleDescription.title)
        Assertions.assertEquals("Roles", data.titleDescription.description)
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
        Assertions.assertEquals(listOf("dima", "anna"), data.watchers)
    }

    @Test fun `extract priority`() {
        val description = """
            Roles to Activities 
            We upgrade (and obvious rename) high priority
        """.trimIndent()
        val data = parser.parse(description)
        Assertions.assertEquals("high", data.priority)
    }

    @Test fun `extract priority in russian`() {
        val description = """
            Конвертировать роли в активности
            Необходима конвертация ролей, высокий приоритет
        """.trimIndent()
        val data = parser.parse(description)
        Assertions.assertEquals("high", data.priority)
    }

    @Test fun `extract labels`() {
        val description = """
            Roles to Activities 
            We upgrade (and obvious rename) high priority *urgent* *backend* some text *bug* mor words * and else **
        """.trimIndent()
        val data = parser.parse(description)
        Assertions.assertEquals(listOf("urgent", "backend", "bug"), data.labels)
    }

    @Test fun `extract dates`() {
        val description = """
            Roles to Activities 
            We upgrade (and obvious rename) due date 12/05/2020
        """.trimIndent()
        val data = parser.parse(description)
        Assertions.assertEquals(
            LocalDateTime.of(2020, 5, 12, 0, 0),
            data.dueDate)
    }

    @Test fun `extract date time`() {
        val description = """
            Roles to Activities 
            We upgrade (and obvious rename) due date 12/05/2020 15:00
        """.trimIndent()
        val data = parser.parse(description)
        Assertions.assertEquals(
            LocalDateTime.of(2020, 5, 12, 15, 0),
            data.dueDate)
    }
}
