package io.easybreezy.hr.api.controller

import io.easybreezy.createMember
import io.easybreezy.hr.createLocation
import io.easybreezy.hr.createUserLocation
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

class LocationControllerTest {

    @Test
    fun `location create`() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {
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
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {
                val locationId = database.createLocation()

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
    fun `locations`() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {
                database.createLocation()

                with(handleRequest(HttpMethod.Get, "/api/hr/locations")) {
                    Assertions.assertTrue(response.content?.contains("Best Location For a Job")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `user location assign`() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {
                val locationId = database.createLocation()
                val userId = database.createMember()

                with(handleRequest(HttpMethod.Post, "/api/hr/locations/user") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "userId" to userId.toString()
                            "startedAt" to "2020-07-19"
                            "endedAt" to "2020-08-19"
                            "locationId" to locationId.toString()
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/locations/user?from=2010-04-04&to=2030-04-04")) {
                    Assertions.assertTrue(response.content?.contains("Best Location For a Job")!!)
                    Assertions.assertTrue(response.content?.contains("2020-08-19")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `user location edit`() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {
                val locationId = database.createLocation()
                val userId = database.createMember()
                val userLocationId = database.createUserLocation(userId, locationId)

                with(handleRequest(HttpMethod.Post, "/api/hr/locations/user/$userLocationId") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "startedAt" to "2020-03-19"
                            "endedAt" to "2020-04-19"
                            "locationId" to locationId.toString()
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/locations/user?from=2010-04-04&to=2030-04-04")) {
                    Assertions.assertTrue(response.content?.contains("2020-03-19")!!)
                    Assertions.assertTrue(response.content?.contains("2020-04-19")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `user location show`() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {
                val locationId = database.createLocation()
                val userId = database.createMember()
                val userLocationId = database.createUserLocation(userId, locationId)

                with(handleRequest(HttpMethod.Get, "/api/hr/locations/user/$userLocationId")) {
                    Assertions.assertTrue(response.content?.contains("Best Location For a Job")!!)
                    Assertions.assertTrue(response.content?.contains(userId.toString())!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `user locations listing`() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {
                val locationId = database.createLocation()
                val userId = database.createMember()
                database.createUserLocation(userId, locationId)

                with(handleRequest(HttpMethod.Get, "/api/hr/locations/user?from=2010-04-04&to=2030-04-04")) {
                    Assertions.assertTrue(response.content?.contains("Best Location For a Job")!!)
                    Assertions.assertTrue(response.content?.contains(userId.toString())!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}
