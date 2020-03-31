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

class TeamControllerTest {

    @Test fun `add team to project`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            val project = testDatabase.createMyProject().value
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/teams") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "name" to "Backend"
                            "project" to project.toString()
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

    @Test fun `add member`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            val project = testDatabase.createMyProject()
            val role = testDatabase.createProjectRole(project, "Dev")
            val team = testDatabase.createProjectTeam(project.value, "Lite")

            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/teams/$team/members") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "role" to role.toString()
                            "user" to userId.toString()
                        }
                        .toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/teams/$team")) {
                    val r = response.content
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("member@gmail.com")!!)
                }
            }
        }
    }
}
