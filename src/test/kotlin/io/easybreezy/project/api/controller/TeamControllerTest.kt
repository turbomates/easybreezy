package io.easybreezy.project.api.controller

import io.easybreezy.*
import io.easybreezy.testDatabase
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlinx.serialization.json.json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TeamControllerTest {

    @Test fun `add team to project`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            val project = testDatabase.createMyProject().value
            withTestApplication({ testApplication(userId, testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/teams/add") {
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

    @Test fun `close team`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            val project = testDatabase.createMyProject()
            val team = testDatabase.createProjectTeam(project.value, "Lite")
            withTestApplication({ testApplication(userId, testDatabase) }) {
                with(handleRequest(HttpMethod.Post, "/api/teams/$team/close") {
                    addHeader("Content-Type", "application/json")
                    setBody(json {}.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/teams/$team")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("Closed")!!)
                }
            }
        }
    }

    @Test fun `activate team`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            val project = testDatabase.createMyProject()
            val team = testDatabase.createProjectTeam(project.value, "Lite")
            withTestApplication({ testApplication(userId, testDatabase) }) {
                with(handleRequest(HttpMethod.Post, "/api/teams/$team/activate") {
                    addHeader("Content-Type", "application/json")
                    setBody(json {}.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/teams/$team")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("Active")!!)
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

            withTestApplication({ testApplication(userId, testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/teams/$team/members/add") {
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
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("member@gmail.com")!!)
                }
            }
        }
    }

    @Test fun `change member role`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            val project = testDatabase.createMyProject()
            val role = testDatabase.createProjectRole(project, "Dev")
            val newRole = testDatabase.createProjectRole(project, "PM")
            val team = testDatabase.createProjectTeam(project.value, "Lite")
            testDatabase.createTeamMember(team, userId, role)
            withTestApplication({ testApplication(userId, testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/teams/$team/members/$userId/change-role") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "newRoleId" to newRole.toString()
                        }
                            .toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/teams/$team")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertFalse(response.content?.contains(role.toString())!!)
                    Assertions.assertTrue(response.content?.contains(newRole.toString())!!)
                }
            }
        }
    }

    @Test fun `remove member`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createMember()
            val project = testDatabase.createMyProject()
            val role = testDatabase.createProjectRole(project, "Dev")
            val team = testDatabase.createProjectTeam(project.value, "Lite")
            testDatabase.createTeamMember(team, userId, role)

            withTestApplication({ testApplication(userId, testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/teams/$team/members/$userId/remove") {
                    addHeader("Content-Type", "application/json")
                    setBody(json {}.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/teams/$team")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertFalse(response.content?.contains("member@gmail.com")!!)
                }
            }
        }
    }
}
