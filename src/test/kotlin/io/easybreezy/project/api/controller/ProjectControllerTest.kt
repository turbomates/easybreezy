package io.easybreezy.project.api.controller

import io.easybreezy.*
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

    @Test fun `add role to project`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            val project = testDatabase.createMyProject()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/projects/$project/roles") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "name" to "Tester"
                            "permissions" to jsonArray {
                                + "testing"
                                + "building"
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

    @Test fun `add team to project`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            val project = testDatabase.createMyProject()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/projects/$project/teams") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "name" to "Backend"
                        }
                            .toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/projects/my-project")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("Backend")!!)
                }
            }
        }
    }

    @Test fun `add member to team`() {
    }
}
