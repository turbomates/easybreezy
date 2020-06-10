package io.easybreezy.project.api.controller

import io.easybreezy.createMember
import io.easybreezy.createMyProject
import io.easybreezy.testDatabase
import io.easybreezy.rollbackTransaction
import io.easybreezy.createProjectCategory
import io.easybreezy.testApplication
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlinx.serialization.json.json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class IssueControllerTest {

    @Test
    fun `add issue`() {
        rollbackTransaction {
            val userId = testDatabase.createMember()
            val project = testDatabase.createMyProject()
            testDatabase.createProjectCategory(project, "Nice category")
            withTestApplication({ testApplication(userId, testDatabase) }) {
                with(handleRequest(HttpMethod.Post, "/api/projects/my-project/issues/add") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "content" to """Important issue.
|                               About important stuff. and it is in <nice category>
|                               Also it labeled as *bug* and *frontend*""".trimMargin()
                        }
                            .toString())
                }) {
                    val p = response.content
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/projects/my-project/issues")) {
                    val p = response.content
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("Important")!!)
                    Assertions.assertTrue(response.content?.contains("#FFFFFF")!!)

                    val issueId = response.content?.let {
                        """(?<="id": ").+?(?=")""".toRegex().find(it)?.value
                    }

                    with(handleRequest(HttpMethod.Get, "/api/projects/my-project/issues/$issueId")) {
                        val p = response.content
                        Assertions.assertEquals(HttpStatusCode.OK, response.status())
                        Assertions.assertTrue(response.content?.contains("bug")!!)
                        Assertions.assertTrue(response.content?.contains("frontend")!!)
                        Assertions.assertTrue(response.content?.contains("Nice category")!!)
                    }
                }
            }
        }
    }
}
