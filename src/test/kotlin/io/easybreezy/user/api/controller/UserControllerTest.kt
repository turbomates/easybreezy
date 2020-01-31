package io.easybreezy.user.api.controller

import com.google.gson.Gson
import io.easybreezy.rollbackTransaction
import io.easybreezy.testApplication
import io.easybreezy.testDatabase
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.UUID

class UserControllerTest {

    @Test
    fun testUserInvite() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {
                with(handleRequest(HttpMethod.Get, "/api/users")) {
                    Assertions.assertEquals(response.status(), HttpStatusCode.OK)
                }

                with(handleRequest(HttpMethod.Post, "/api/users/invite") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        Gson().toJson(
                            mapOf(
                                "email" to "testadmin@testadmin.my",
                                "role" to "MEMBER"
                            )
                        )
                    )
                }) {
                    Assertions.assertEquals(response.status(), HttpStatusCode.OK)
                }

                with(handleRequest(HttpMethod.Get, "/api/users")) {
                    Assertions.assertEquals(response.status(), HttpStatusCode.OK)
                }
            }
        }
    }

    @Test
    fun testUserInviteTwice() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {
                with(handleRequest(HttpMethod.Post, "/api/users/invite") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        Gson().toJson(
                            mapOf(
                                "email" to "testadmin@testadmin.my",
                                "role" to "MEMBER"
                            )
                        )
                    )
                }) {
                    Assertions.assertEquals(response.status(), HttpStatusCode.OK)
                }

                with(handleRequest(HttpMethod.Post, "/api/users/invite") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        Gson().toJson(
                            mapOf(
                                "email" to "testadmin@testadmin.my",
                                "role" to "MEMBER"
                            )
                        )
                    )
                }) {
                    Assertions.assertTrue(response.content?.contains("errors") ?: false)
                    Assertions.assertEquals(response.status(), HttpStatusCode.UnprocessableEntity)
                }
            }
        }
    }
}
