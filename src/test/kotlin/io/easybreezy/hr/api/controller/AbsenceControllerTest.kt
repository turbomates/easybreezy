package io.easybreezy.hr.api.controller

import io.easybreezy.hr.createAbsence
import io.easybreezy.rollbackTransaction
import io.easybreezy.testApplication
import io.easybreezy.testDatabase
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlinx.serialization.json.json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.UUID

class AbsenceControllerTest {

    @Test
    fun `absence create`() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, database) }) {
            rollbackTransaction(database) {
                with(handleRequest(HttpMethod.Post, "/api/hr/absences") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "startedAt" to "2023-07-13"
                            "endedAt" to "2023-08-13"
                            "reason" to "VACATION"
                            "userId" to memberId.toString()
                            "comment" to "Test Comment"
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/hr/absences/me")) {
                    Assertions.assertTrue(response.content?.contains("Test Comment")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `absence update`() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, database) }) {
            rollbackTransaction(database) {
                val absenceId = database.createAbsence(memberId, isApproved = false)

                with(handleRequest(HttpMethod.Post, "/api/hr/absences/$absenceId") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "startedAt" to "2023-08-13"
                            "endedAt" to "2023-10-13"
                            "reason" to "DAYON"
                            "comment" to "Test Comment 2"
                        }.toString()
                    )
                }) {
                    println(response.content)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/hr/absences/me")) {
                    Assertions.assertTrue(response.content?.contains("Test Comment 2")!!)
                    Assertions.assertTrue(response.content?.contains("DAYON")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `approve absence`() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, database) }) {
            rollbackTransaction(database) {
                val absenceId = database.createAbsence(memberId)

                with(handleRequest(HttpMethod.Post, "/api/hr/absences/$absenceId/approve")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/hr/absences?from=2010-04-04&to=2030-04-04")) {
                    Assertions.assertTrue(response.content?.contains("\"isApproved\": true")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `remove absence`() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, database) }) {
            rollbackTransaction(database) {
                val absenceId = database.createAbsence(memberId)

                with(handleRequest(HttpMethod.Delete, "/api/hr/absences/$absenceId")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/hr/absences?from=2010-04-04&to=2030-04-04")) {
                    Assertions.assertFalse(response.content?.contains("Test Comment")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `my absences`() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, database) }) {
            rollbackTransaction(database) {
                database.createAbsence(memberId)

                with(handleRequest(HttpMethod.Get, "/api/hr/absences/me")) {
                    Assertions.assertTrue(response.content?.contains("Test Comment")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `user absences`() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, database) }) {
            rollbackTransaction(database) {
                database.createAbsence(memberId)

                with(handleRequest(HttpMethod.Get, "/api/hr/user/$memberId/absences")) {
                    Assertions.assertTrue(response.content?.contains("Test Comment")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun absences() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, database) }) {
            rollbackTransaction(database) {
                database.createAbsence(memberId)

                with(handleRequest(HttpMethod.Get, "/api/hr/absences?from=2010-04-04&to=2030-04-04")) {
                    Assertions.assertTrue(response.content?.contains("Test Comment")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun show() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, database) }) {
            rollbackTransaction(database) {
                val absenceId = database.createAbsence(memberId)

                with(handleRequest(HttpMethod.Get, "/api/hr/absences/$absenceId")) {
                    Assertions.assertTrue(response.content?.contains("Test Comment")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}
