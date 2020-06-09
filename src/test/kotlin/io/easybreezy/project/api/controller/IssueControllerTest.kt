package io.easybreezy.project.api.controller

import io.easybreezy.*
import io.easybreezy.createIssue
import io.easybreezy.createMember
import io.easybreezy.createMyProject
import io.easybreezy.createProjectCategory
import io.easybreezy.testDatabase
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

    @Test
    fun `change status`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            val project = testDatabase.createMyProject()
            val newStatus = testDatabase.createProjectStatus(project, "In progress")
            val issue = testDatabase.createIssue(project.value)
            withTestApplication({ testApplication(userId, testDatabase) }) {
                with(handleRequest(HttpMethod.Post, "/api/projects/my-project/issues/$issue/change-status") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "newStatus" to newStatus.toString()
                        }
                            .toString())
                }) {
                    val p = response.content
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/projects/my-project/issues/$issue")) {
                    val p = response.content
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("In progress")!!)
                }
            }
        }
    }

    @Test
    fun `update issue by comment`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            val project = testDatabase.createMyProject()
            val team = testDatabase.createProjectTeam(project.value, "team")
            val role = testDatabase.createProjectRole(project, "role")
            testDatabase.createTeamMember(team, userId, role)
            val issue = testDatabase.createIssue(project.value)
            withTestApplication({ testApplication(userId, testDatabase) }) {
                with(handleRequest(HttpMethod.Post, "/api/projects/my-project/issues/$issue/comment") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "content" to "high priority and now ->@johndoe is responsible"
                        }
                            .toString())
                }) {
                    val p = response.content
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/projects/my-project/issues/$issue")) {
                    val p = response.content
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains(userId.toString())!!)
                    Assertions.assertTrue(response.content?.contains("#FF0000")!!)
                    Assertions.assertTrue(response.content?.contains("comments")!!)
                }
            }
        }
    }
    @Test
    fun `sub issue`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            val project = testDatabase.createMyProject()
            val issue = testDatabase.createIssue(project.value)
            withTestApplication({ testApplication(userId, testDatabase) }) {
                with(handleRequest(HttpMethod.Post, "/api/projects/my-project/issues/$issue/sub-issue") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "content" to """Important sub issue
                                    dsdsdsds
                                    erewrer""".trimMargin()
                        }
                            .toString())
                }) {
                    val p = response.content
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/projects/my-project/issues")) {
                    val p = response.content
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("Important sub issue")!!)
                    Assertions.assertTrue(response.content?.contains("parent\": \"$issue")!!)
                }
            }
        }
    }
}
