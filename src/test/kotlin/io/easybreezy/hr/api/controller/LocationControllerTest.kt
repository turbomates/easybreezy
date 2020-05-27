package io.easybreezy.hr.api.controller

import io.easybreezy.createMember
import io.easybreezy.hr.createLocation
import io.easybreezy.hr.createUserLocation
import io.easybreezy.rollbackTransaction
import io.easybreezy.testApplication
import io.easybreezy.testDatabase
import io.easybreezy.testDateFormatter
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlinx.serialization.json.json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LocationControllerTest {

    @Test
    fun `location create`() {
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                with(handleRequest(HttpMethod.Post, "/api/hr/locations") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "name" to "UK"
                            "vacationDays" to 25
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/hr/locations")) {
                    Assertions.assertTrue(response.content?.contains("UK")!!)
                    Assertions.assertTrue(response.content?.contains("25")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `remove location`() {
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                val locationId = testDatabase.createLocation()

                with(handleRequest(HttpMethod.Delete, "/api/hr/locations/$locationId")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/locations")) {
                    Assertions.assertFalse(response.content?.contains("Best Location For a Job")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun locations() {
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                testDatabase.createLocation()

                with(handleRequest(HttpMethod.Get, "/api/hr/locations")) {
                    Assertions.assertTrue(response.content?.contains("Best Location For a Job")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `user location assign`() {
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                val locationId = testDatabase.createLocation()
                val userId = testDatabase.createMember()

                with(handleRequest(HttpMethod.Post, "/api/hr/user-locations") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "userId" to userId.toString()
                            "startedAt" to LocalDate.now().format(testDateFormatter)
                            "locationId" to locationId.toString()
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/user-locations?from=2010-04-04&to=2030-04-04")) {
                    Assertions.assertTrue(response.content?.contains("Best Location For a Job")!!)
                    Assertions.assertTrue(response.content?.contains(LocalDate.now().format(testDateFormatter))!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `user assign new location and previous should be closed`() {
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                val locationId = testDatabase.createLocation()
                val userId = testDatabase.createMember()
                val userLocationId = testDatabase.createUserLocation(userId, locationId, endedAt = null)

                with(handleRequest(HttpMethod.Post, "/api/hr/user-locations") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "userId" to userId.toString()
                            "startedAt" to LocalDate.now().format(testDateFormatter)
                            "locationId" to locationId.toString()
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/user-locations/$userLocationId")) {
                    Assertions.assertTrue(response.content?.contains(LocalDate.now().format(testDateFormatter))!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `user location edit`() {
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                val locationId = testDatabase.createLocation()
                val userId = testDatabase.createMember()
                val userLocationId = testDatabase.createUserLocation(userId, locationId)

                with(handleRequest(HttpMethod.Post, "/api/hr/user-locations/$userLocationId") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "startedAt" to LocalDate.now().format(testDateFormatter)
                            "locationId" to locationId.toString()
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/user-locations?from=2010-04-04&to=2030-04-04")) {
                    Assertions.assertTrue(response.content?.contains(LocalDate.now().format(testDateFormatter))!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `user location show`() {
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                val locationId = testDatabase.createLocation()
                val userId = testDatabase.createMember()
                val userLocationId = testDatabase.createUserLocation(userId, locationId)

                with(handleRequest(HttpMethod.Get, "/api/hr/user-locations/$userLocationId")) {
                    Assertions.assertTrue(response.content?.contains("Best Location For a Job")!!)
                    Assertions.assertTrue(response.content?.contains(userId.toString())!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `users locations listing`() {
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                val locationId = testDatabase.createLocation()
                val userId = testDatabase.createMember()
                testDatabase.createUserLocation(userId, locationId)

                with(handleRequest(HttpMethod.Get, "/api/hr/user-locations?from=2010-04-04&to=2030-04-04")) {
                    Assertions.assertTrue(response.content?.contains("Best Location For a Job")!!)
                    Assertions.assertTrue(response.content?.contains(userId.toString())!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `test user locations`() {
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                val locationId = testDatabase.createLocation()
                val userId = testDatabase.createMember()
                testDatabase.createUserLocation(userId, locationId)

                with(handleRequest(HttpMethod.Get, "/api/hr/user/$userId/locations")) {
                    Assertions.assertTrue(response.content?.contains("Best Location For a Job")!!)
                    Assertions.assertTrue(response.content?.contains(userId.toString())!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}
