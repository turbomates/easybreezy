package io.easybreezy.user.api.controller

import io.easybreezy.createMember
import io.easybreezy.infrastructure.ktor.auth.Activity
import io.easybreezy.rollbackTransaction
import io.easybreezy.testApplication
import io.easybreezy.testDatabase
import io.easybreezy.user.model.Status
import io.easybreezy.withSwagger
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.json
import kotlinx.serialization.json.jsonArray
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UserControllerTest {

    @Test
    fun `user invite`() {
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                withSwagger(handleRequest(HttpMethod.Get, "/api/users")) {
                    Assertions.assertEquals(response.status(), HttpStatusCode.OK)
                }

                withSwagger(handleRequest(HttpMethod.Post, "/api/users/invite") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "email" to "testadmin@testadmin.my"
                            "activities" to jsonArray {
                                +JsonPrimitive(Activity.USERS_MANAGE.name)
                            }
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
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                with(handleRequest(HttpMethod.Post, "/api/users/invite") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "email" to "testadmin@testadmin.my"
                            "activities" to jsonArray {
                                +JsonPrimitive(Activity.USERS_MANAGE.name)
                            }
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
                            "activities" to jsonArray {
                                +JsonPrimitive(Activity.USERS_MANAGE.name)
                            }
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
        withTestApplication({ testApplication() }) {
            rollbackTransaction {

                withSwagger(handleRequest(HttpMethod.Post, "/api/users") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "email" to "candidate@gmail.com"
                            "activities" to jsonArray {
                                +JsonPrimitive(Activity.USERS_MANAGE.name)
                            }
                            "firstName" to "Interesting"
                            "lastName" to "Candidate"
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(response.status(), HttpStatusCode.OK)
                }

                withSwagger(handleRequest(HttpMethod.Get, "/api/users")) {
                    Assertions.assertEquals(response.status(), HttpStatusCode.OK)
                    Assertions.assertTrue(response.content?.contains(Status.PENDING.name) ?: false)
                    Assertions.assertTrue(response.content?.contains("candidate") ?: false)
                }
            }
        }
    }

    @Test
    fun `invite pending user`() {
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                val userId = testDatabase.createMember(status = Status.PENDING)

                withSwagger(handleRequest(HttpMethod.Post, "/api/users/$userId/hire")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                withSwagger(handleRequest(HttpMethod.Get, "/api/users")) {
                    Assertions.assertEquals(response.status(), HttpStatusCode.OK)
                    Assertions.assertTrue(response.content?.contains(Status.WAIT_CONFIRM.name) ?: false)
                }
            }
        }
    }

    @Test
    fun `archive pending user`() {
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                val userId = testDatabase.createMember(status = Status.PENDING)
                val reason = "Some reason to archive"

                withSwagger(handleRequest(HttpMethod.Post, "/api/users/$userId/archive") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "reason" to reason
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                withSwagger(handleRequest(HttpMethod.Get, "/api/users")) {
                    Assertions.assertEquals(response.status(), HttpStatusCode.OK)
                    Assertions.assertTrue(response.content?.contains(Status.ARCHIVED.name) ?: false)
                    Assertions.assertTrue(response.content?.contains(reason) ?: false)
                }
            }
        }
    }

    @Test
    fun `update user activities`() {
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                val userId = testDatabase.createMember()

                withSwagger(handleRequest(HttpMethod.Post, "/api/users/$userId/activities") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "activities" to jsonArray {
                                +JsonPrimitive(Activity.USERS_MANAGE.name)
                                +JsonPrimitive(Activity.PROJECTS_SHOW.name)
                            }
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                withSwagger(handleRequest(HttpMethod.Get, "/api/users")) {
                    Assertions.assertEquals(response.status(), HttpStatusCode.OK)
                    Assertions.assertTrue(response.content?.contains(Activity.USERS_MANAGE.name) ?: false)
                    Assertions.assertTrue(response.content?.contains(Activity.PROJECTS_SHOW.name) ?: false)
                }
            }
        }
    }

    @Test
    fun `change username`() {
        rollbackTransaction {
            val userId = testDatabase.createMember()
            withTestApplication({ testApplication(userId) }) {
                withSwagger(handleRequest(HttpMethod.Post, "/api/users/change-username") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "username" to "new-username"
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                withSwagger(handleRequest(HttpMethod.Get, "/api/users")) {
                    Assertions.assertEquals(response.status(), HttpStatusCode.OK)
                    Assertions.assertTrue(response.content?.contains("new-username") ?: false)
                }
            }
        }
    }
}
