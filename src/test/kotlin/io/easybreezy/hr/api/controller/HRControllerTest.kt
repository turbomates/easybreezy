package io.easybreezy.hr.api.controller

import io.easybreezy.createEmployee
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

class HRControllerTest {

    @Test fun `hire employee`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createEmployee()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/hr/employee/$userId/hire") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "position" to "PM"
                            "salary" to 1000
                            "birthday" to "2000-01-01"
                            "bio" to "Employee's nice bio"
                            "skills" to jsonArray { +"Scrum" }
                        }
                        .toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/employee/$userId")) {
                    println(response.content)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("PM")!!)
                    Assertions.assertTrue(response.content?.contains("1000")!!)
                    Assertions.assertTrue(response.content?.contains("2000-01-01")!!)
                    Assertions.assertTrue(response.content?.contains("Employee's nice bio")!!)
                    Assertions.assertTrue(response.content?.contains("Scrum")!!)
                }
            }
        }
    }

    @Test fun `fire employee`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createEmployee()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/hr/employee/$userId/fire") {
                    addHeader("Content-Type", "application/json")
                    setBody(json { "comment" to "was fired" }.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/employees")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertFalse(response.content?.contains("$userId")!!)
                }
            }
        }
    }

    @Test fun `add employee note`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createEmployee()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/hr/employee/$userId/write-note") {
                    addHeader("Content-Type", "application/json")
                    setBody(json { "text" to "note about employee" }.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/employee/$userId")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("note about employee")!!)
                }
            }
        }
    }

    @Test fun `add employee position`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createEmployee()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/hr/employee/$userId/apply-position") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json { "position" to "Developer" }.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/employee/$userId")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("Developer")!!)
                }
            }
        }
    }

    @Test fun `keep employee position history`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createEmployee()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/hr/employee/$userId/apply-position") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json { "position" to "position1" }.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Post, "/api/hr/employee/$userId/apply-position") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json { "position" to "position2" }.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/employee/$userId")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("position1")!!)
                    Assertions.assertTrue(response.content?.contains("position2")!!)
                }
            }
        }
    }

    @Test fun `add employee salary`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createEmployee()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/hr/employee/$userId/apply-salary") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "amount" to 1200
                            "comment" to "raise salary"
                        }.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/employee/$userId")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("1200")!!)
                    Assertions.assertTrue(response.content?.contains("raise salary")!!)
                }
            }
        }
    }

    @Test fun `keep employee salary history`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createEmployee()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/hr/employee/$userId/apply-salary") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "amount" to 1200
                            "comment" to "raise salary"
                        }.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Post, "/api/hr/employee/$userId/apply-salary") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "amount" to 1250
                            "comment" to "raise salary2"
                        }.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/employee/$userId")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("1200")!!)
                    Assertions.assertTrue(response.content?.contains("1250")!!)
                }
            }
        }
    }

    @Test fun `add employee skills`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createEmployee()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/hr/employee/$userId/specify-skills") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "skills" to jsonArray {
                                + "Kotlin"
                                + "Ktor"
                            }
                        }.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/employee/$userId")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("Kotlin")!!)
                    Assertions.assertTrue(response.content?.contains("Ktor")!!)
                }
            }
        }
    }

    @Test fun `update employee bio`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createEmployee()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/hr/employee/$userId/update-bio") {
                    addHeader("Content-Type", "application/json")
                    setBody(json { "bio" to "My nice bio" }.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/employee/$userId")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("My nice bio")!!)
                }
            }
        }
    }

    @Test fun `update employee birthday`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createEmployee()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/hr/employee/$userId/update-birthday") {
                    addHeader("Content-Type", "application/json")
                    setBody(json { "birthday" to "2001-01-01" }.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/employee/$userId")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("2001-01-01")!!)
                }
            }
        }
    }

    @Test fun `update employee contacts`() {
        rollbackTransaction(testDatabase) {
            val userId = testDatabase.createEmployee()
            withTestApplication({ testApplication(userId, emptySet(), testDatabase) }) {

                with(handleRequest(HttpMethod.Post, "/api/users/update-contacts") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "contacts" to jsonArray {
                                +json {
                                    "type" to "SKYPE"
                                    "value" to "skype-n"
                                }
                            }
                        }.toString())
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/employee/$userId")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("skype-n")!!)
                }
            }
        }
    }
}
