package io.easybreezy.user.api.controller

import io.easybreezy.*
import io.easybreezy.testDatabase
import io.easybreezy.user.model.Status
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlinx.serialization.json.json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.UUID

class UserControllerTest {

    @Test
    fun `user invite`() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {
                withSwagger(handleRequest(HttpMethod.Get, "/api/users")) {
                    Assertions.assertEquals(response.status(), HttpStatusCode.OK)
                }

                withSwagger(handleRequest(HttpMethod.Post, "/api/users/invite") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "email" to "testadmin@testadmin.my"
                            "role" to "MEMBER"
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(response.status(), HttpStatusCode.OK)
                }
            }
        }
    }

    @Test
    fun `user invite twice`() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {
                with(handleRequest(HttpMethod.Post, "/api/users/invite") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "email" to "testadmin@testadmin.my"
                            "role" to "MEMBER"
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(response.status(), HttpStatusCode.OK)
                }

                with(handleRequest(HttpMethod.Post, "/api/users/invite") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "email" to "testadmin@testadmin.my"
                            "role" to "MEMBER"
                        }.toString()
                    )
                }) {
                    Assertions.assertTrue(response.content?.contains("errors") ?: false)
                    Assertions.assertEquals(response.status(), HttpStatusCode.UnprocessableEntity)
                }
            }
        }
    }

    @Test
    fun `user create`() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {

                withSwagger(handleRequest(HttpMethod.Post, "/api/users") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "email" to "candidate@gmail.com"
                            "role" to "MEMBER"
                            "firstName" to "Interesting"
                            "lastName" to "Candidate"
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(response.status(), HttpStatusCode.OK)
                }

                withSwagger(handleRequest(HttpMethod.Get, "/api/users")) {
                    Assertions.assertEquals(response.status(), HttpStatusCode.OK)
                    Assertions.assertTrue(response.content?.contains("CREATED") ?: false)
                    Assertions.assertTrue(response.content?.contains("candidate") ?: false)
                }
            }
        }
    }

    @Test
    fun `invite already created user`() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {
                val userId = database.createMember(status = Status.CREATED)

                withSwagger(handleRequest(HttpMethod.Post, "/api/users/$userId/invite")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                withSwagger(handleRequest(HttpMethod.Get, "/api/users")) {
                    println(response.content)
                    Assertions.assertEquals(response.status(), HttpStatusCode.OK)
                    Assertions.assertTrue(response.content?.contains("WAIT_CONFIRM") ?: false)
                }
            }
        }
    }
}
