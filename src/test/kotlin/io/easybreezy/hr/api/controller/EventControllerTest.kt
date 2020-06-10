package io.easybreezy.hr.api.controller

import io.easybreezy.createMember
import io.easybreezy.hr.createEvent
import io.easybreezy.hr.createLocation
import io.easybreezy.hr.createParticipant
import io.easybreezy.hr.model.event.Status
import io.easybreezy.hr.model.event.VisitStatus
import io.easybreezy.rollbackTransaction
import io.easybreezy.testApplication
import io.easybreezy.testDatabase
import io.easybreezy.testDateTimeFormatter
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlinx.serialization.json.json
import kotlinx.serialization.json.jsonArray
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class EventControllerTest {

    @Test
    fun `open event with minimum settings`() {
        rollbackTransaction {
            val memberId = testDatabase.createMember()
            val name = "Davids meeting"
            withTestApplication({ testApplication(memberId) }) {
                with(handleRequest(HttpMethod.Post, "/api/hr/events") {

                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "name" to name
                            "startedAt" to LocalDateTime.now().plusDays(5).format(testDateTimeFormatter)
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/hr/events")) {
                    Assertions.assertTrue(response.content?.contains(name)!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `open event invalid startedAt`() {
        rollbackTransaction {
            val memberId = testDatabase.createMember()
            withTestApplication({ testApplication(memberId) }) {
                with(handleRequest(HttpMethod.Post, "/api/hr/events") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "name" to "Davids meeting"
                            "startedAt" to LocalDateTime.now().minusDays(5).format(testDateTimeFormatter)
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.UnprocessableEntity, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/hr/events")) {
                    Assertions.assertFalse(response.content?.contains("Davids meeting")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    // @Test
    // fun `open event with maximum settings`() {
    //     rollbackTransaction {
    //         val memberId = testDatabase.createMember()
    //         withTestApplication({ testApplication(memberId) }) {
    //             val locationId = testDatabase.createLocation()
    //             val startedAt = LocalDateTime.now().plusDays(5).format(testDateTimeFormatter)
    //             val name = "Peters meeting"
    //             val description = "Meeting description"
    //             with(handleRequest(HttpMethod.Post, "/api/hr/events") {
    //                 addHeader("Content-Type", "application/json")
    //                 setBody(
    //                     json {
    //                         "name" to name
    //                         "startedAt" to startedAt
    //                         "description" to description
    //                         "endedAt" to LocalDateTime.now().plusDays(5).format(testDateTimeFormatter)
    //                         "location" to locationId
    //                         "participants" to jsonArray { +memberId.toString() }
    //                         "isPrivate" to false
    //                         "isFreeEntry" to true
    //                         "isRepeatable" to false
    //                         "days" to jsonArray {
    //                             +"MONDAY"
    //                             +"FRIDAY"
    //                         }
    //                     }.toString()
    //                 )
    //             }) {
    //                 Assertions.assertEquals(HttpStatusCode.OK, response.status())
    //             }
    //             with(handleRequest(HttpMethod.Get, "/api/hr/events")) {
    //                 Assertions.assertTrue(response.content?.contains(name)!!)
    //                 Assertions.assertTrue(response.content?.contains(description)!!)
    //                 Assertions.assertTrue(response.content?.contains(startedAt)!!)
    //                 Assertions.assertTrue(response.content?.contains("MONDAY")!!)
    //                 Assertions.assertEquals(HttpStatusCode.OK, response.status())
    //             }
    //         }
    //     }
    // }

    @Test
    fun `update event details`() {
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                val eventId = testDatabase.createEvent()
                val name = "Edited Name"
                val startedAt = LocalDateTime.now().plusDays(15).format(testDateTimeFormatter)
                val endedAt = LocalDateTime.now().plusDays(25).format(testDateTimeFormatter)
                val description = "Edited Description"
                val locationId = testDatabase.createLocation()
                with(handleRequest(HttpMethod.Post, "/api/hr/events/$eventId") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "name" to name
                            "startedAt" to startedAt
                            "endedAt" to endedAt
                            "description" to description
                            "location" to locationId.toString()
                            "days" to jsonArray {
                                +"MONDAY"
                                +"FRIDAY"
                            }
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/hr/events")) {
                    Assertions.assertTrue(response.content?.contains(name)!!)
                    Assertions.assertTrue(response.content?.contains(startedAt)!!)
                    Assertions.assertTrue(response.content?.contains(endedAt)!!)
                    Assertions.assertTrue(response.content?.contains(description)!!)
                    Assertions.assertTrue(response.content?.contains("Best Location For a Job")!!)
                    Assertions.assertTrue(response.content?.contains("FRIDAY")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `change some event conditions`() {
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                val eventId = testDatabase.createEvent()
                with(handleRequest(HttpMethod.Post, "/api/hr/events/$eventId/conditions") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "isFreeEntry" to false
                            "isRepeatable" to true
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/hr/events")) {
                    Assertions.assertTrue(response.content?.contains("\"isRepeatable\": true")!!)
                    Assertions.assertTrue(response.content?.contains("\"isFreeEntry\": false")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `the user can't see someone else's private event`() {
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                val eventId = testDatabase.createEvent(true)

                with(handleRequest(HttpMethod.Get, "/api/hr/events")) {
                    Assertions.assertFalse(response.content?.contains(eventId.toString())!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `add participants to the event`() {
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                val eventId = testDatabase.createEvent()
                val memberOne = testDatabase.createMember(email = "memberOne@gmail.com")
                val memberTwo = testDatabase.createMember(email = "memberTwo@gmail.com")
                with(handleRequest(HttpMethod.Post, "/api/hr/events/$eventId/participants") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "participants" to jsonArray {
                                +memberOne.toString()
                                +memberTwo.toString()
                            }
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/hr/events/$eventId")) {
                    Assertions.assertTrue(response.content?.contains(memberOne.toString())!!)
                    Assertions.assertTrue(response.content?.contains(memberTwo.toString())!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `employee enter to event by himself`() {
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                val eventId = testDatabase.createEvent()
                val memberOne = testDatabase.createMember(email = "memberOne@gmail.com")
                with(handleRequest(HttpMethod.Post, "/api/hr/events/$eventId/participants/enter") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "employeeId" to memberOne.toString()
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/hr/events/$eventId")) {
                    Assertions.assertTrue(response.content?.contains(memberOne.toString())!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `employee change his visit status`() {
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                val employeeId = testDatabase.createMember(email = "participant@gmail.com")
                val eventId = testDatabase.createEvent()
                testDatabase.createParticipant(eventId, employeeId)
                with(handleRequest(HttpMethod.Post, "/api/hr/events/$eventId/participants/visit-status") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "employeeId" to employeeId.toString()
                            "status" to VisitStatus.MAYBE_COME.name
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/hr/events/$eventId")) {
                    Assertions.assertTrue(response.content?.contains(VisitStatus.MAYBE_COME.name)!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `cancel event`() {
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                val eventId = testDatabase.createEvent()
                with(handleRequest(HttpMethod.Post, "/api/hr/events/$eventId/cancel")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/hr/events/$eventId")) {
                    Assertions.assertTrue(response.content?.contains(Status.CANCELLED.name)!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}
