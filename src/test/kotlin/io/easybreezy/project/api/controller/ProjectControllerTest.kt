package io.easybreezy.project.api.controller

import io.easybreezy.createMember
import io.easybreezy.createMyProject
import io.easybreezy.createProjectCategory
import io.easybreezy.createProjectRole
import io.easybreezy.createProjectTeam
import io.easybreezy.project.model.team.Role
import io.easybreezy.rollbackTransaction
import io.easybreezy.testApplication
import io.easybreezy.testDatabase
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlinx.serialization.json.json
import kotlinx.serialization.json.jsonArray
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ProjectControllerTest {

    @Test fun `add project`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/projects") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "name" to "My Project"
                            "description" to "Project description"
                        }
                        .toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/projects/my-project")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("My Project")!!)
                }
            }
        }
    }

    @Test fun `list of projects`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            testDatabase.createMyProject()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Get, "/api/projects")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("My Project")!!)
                }
            }
        }
    }

    @Test fun `suspend project`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            testDatabase.createMyProject()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {
                with(handleRequest(HttpMethod.Post, "/api/projects/my-project/suspend") {
                    addHeader("Content-Type", "application/json")
                    setBody(json {}.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/projects/my-project")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("Suspended")!!)
                }
            }
        }
    }

    @Test fun `close project`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            testDatabase.createMyProject()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {
                with(handleRequest(HttpMethod.Post, "/api/projects/my-project/close") {
                    addHeader("Content-Type", "application/json")
                    setBody(json {}.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/projects/my-project")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("Closed")!!)
                }
            }
        }
    }

    @Test fun `activate project`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            testDatabase.createMyProject()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {
                with(handleRequest(HttpMethod.Post, "/api/projects/my-project/suspend") {
                    addHeader("Content-Type", "application/json")
                    setBody(json {}.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Post, "/api/projects/my-project/activate") {
                    addHeader("Content-Type", "application/json")
                    setBody(json {}.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/projects/my-project")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("Active")!!)
                }
            }
        }
    }

    @Test fun `write description to project`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            testDatabase.createMyProject()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {
                with(handleRequest(HttpMethod.Post, "/api/projects/my-project/write-description") {
                    addHeader("Content-Type", "application/json")
                    setBody(json {
                        "description" to "My project for my needs"
                    }.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/projects/my-project")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("My project for my needs")!!)
                }
            }
        }
    }

    @Test fun `add role to project`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            testDatabase.createMyProject()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {
                with(handleRequest(HttpMethod.Post, "/api/projects/my-project/roles/add") {
                    addHeader("Content-Type", "application/json")
                    setBody(json {
                        "name" to "Tester"
                        "permissions" to jsonArray {
                            + Role.Permission.PROJECT.name
                            + Role.Permission.TEAM.name
                        }
                    }
                        .toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/projects/my-project")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("Tester")!!)
                }
            }
        }
    }

    @Test fun `change role of project`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            val project = testDatabase.createMyProject()
            val roleId = testDatabase.createProjectRole(project, "Tester")
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/projects/my-project/roles/$roleId/change") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "permissions" to jsonArray {
                                + Role.Permission.PROJECT.name
                            }
                        }
                            .toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/projects/my-project")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains(Role.Permission.PROJECT.name)!!)
                }
            }
        }
    }

    @Test fun `remove role from project`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            val project = testDatabase.createMyProject()
            val roleId = testDatabase.createProjectRole(project, "Tester")
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/projects/my-project/roles/$roleId/remove") {
                    addHeader("Content-Type", "application/json")
                    setBody(json {}.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/projects/my-project")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertFalse(response.content?.contains("Tester")!!)
                }
            }
        }
    }

    @Test fun `try to remove role from project with members`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            val project = testDatabase.createMyProject()
            val roleId = testDatabase.createProjectRole(project, "Tester")
            val team = testDatabase.createProjectTeam(project.value, "Lite")
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/teams/$team/members/add") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "role" to roleId.toString()
                            "user" to userId.toString()
                        }
                            .toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Post, "/api/projects/my-project/roles/$roleId/remove") {
                    addHeader("Content-Type", "application/json")
                    setBody(json {}.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.UnprocessableEntity, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/projects/my-project")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("Tester")!!)
                }
            }
        }
    }

    @Test fun `add category to project`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            testDatabase.createMyProject()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {
                with(handleRequest(HttpMethod.Post, "/api/projects/my-project/categories/add") {
                    addHeader("Content-Type", "application/json")
                    setBody(json {
                        "name" to "Epic"
                    }
                        .toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/projects/my-project")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("Epic")!!)
                }
            }
        }
    }

    @Test fun `change category of project`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            val project = testDatabase.createMyProject()
            val parent = testDatabase.createProjectCategory(project, "Epic")
            val category = testDatabase.createProjectCategory(project, "Feature")
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/projects/my-project/categories/$category/change") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "name" to "Feature"
                            "parent" to parent.toString()
                        }
                            .toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/projects/my-project")) {

                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("Feature")!!)
                }
            }
        }
    }

    @Test fun `remove category from project`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            val project = testDatabase.createMyProject()
            val category = testDatabase.createProjectCategory(project, "TestCat")
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/projects/my-project/categories/$category/remove") {
                    addHeader("Content-Type", "application/json")
                    setBody(json {}.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/projects/my-project")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertFalse(response.content?.contains("TestCat")!!)
                }
            }
        }
    }
}
