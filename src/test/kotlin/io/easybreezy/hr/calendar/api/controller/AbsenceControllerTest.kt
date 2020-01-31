package io.easybreezy.hr.calendar.api.controller

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

class AbsenceControllerTest {
    @Test
    fun testAbsenceCreate() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {
                with(handleRequest(HttpMethod.Post, "/api/hr/absences") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        Gson().toJson(
                            mapOf(
                                "startedAt" to "2023-07-13",
                                "endedAt" to "2023-08-13",
                                "reason" to "VACATION",
                                "userId" to memberId,
                                "comment" to "Test Comment",
                                "location" to "Belarus"
                            )
                        )
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/hr/absences")) {
                    println(response.content)
                    Assertions.assertTrue(response.content?.contains("Test Comment")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}
