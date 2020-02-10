package io.easybreezy.hr.calendar.api.controller

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
    fun testAbsenceCreate() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {
                with(handleRequest(HttpMethod.Post, "/api/hr/locations") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "name" to "UK"
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/hr/locations")) {
                    Assertions.assertTrue(response.content?.contains("UK")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun testRemoveLocation() {
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
    fun testLocationsListing() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {
                database.createLocation()

                with(handleRequest(HttpMethod.Get, "/api/hr/locations")) {
                    println(response.content)
                    Assertions.assertTrue(response.content?.contains("Best Location For a Job")!!)
                    Assertions.assertTrue(response.content?.contains("hasMore")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }



    @Test
    fun testUserLocationAssign() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {
                val locationId = database.createLocation()

                with(handleRequest(HttpMethod.Post, "/api/hr/locations/user") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "startedAt" to "2020-07-19"
                            "endedAt" to "2020-08-19"
                            "locationId" to locationId.toString()
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/locations/user")) {
                    println(response.content)
                    Assertions.assertTrue(response.content?.contains("Best Location For a Job")!!)
                    Assertions.assertTrue(response.content?.contains("2020-08-19")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun testLocationEdit() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {
                val locationId = database.createLocation()
                val userLocationId = database.createUserLocation(memberId, locationId)

                with(handleRequest(HttpMethod.Post, "/api/hr/locations/user/$userLocationId") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "startedAt" to "2021-07-19"
                            "endedAt" to "2023-08-19"
                            "locationId" to locationId.toString()
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/locations/user")) {
                    println(response.content)
                    Assertions.assertTrue(response.content?.contains("2021-07-19")!!)
                    Assertions.assertTrue(response.content?.contains("2023-08-19")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun testRemoveUserLocation() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {
                val locationId = database.createLocation()
                val userLocationId = database.createUserLocation(memberId, locationId)

                with(handleRequest(HttpMethod.Delete, "/api/hr/locations/user/$userLocationId")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/locations/user")) {
                    Assertions.assertFalse(response.content?.contains("Best Location For a Job")!!)
                    Assertions.assertFalse(response.content?.contains(memberId.toString())!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun testUserLocationShow() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {
                val locationId = database.createLocation()
                val userLocationId = database.createUserLocation(memberId, locationId)

                with(handleRequest(HttpMethod.Get, "/api/hr/locations/user/$userLocationId")) {
                    Assertions.assertTrue(response.content?.contains("Best Location For a Job")!!)
                    Assertions.assertTrue(response.content?.contains(memberId.toString())!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun testUserLocationsListing() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {
                val locationId = database.createLocation()
                database.createUserLocation(memberId, locationId)

                with(handleRequest(HttpMethod.Get, "/api/hr/locations/user")) {
                    Assertions.assertTrue(response.content?.contains("Best Location For a Job")!!)
                    Assertions.assertTrue(response.content?.contains(memberId.toString())!!)
                    Assertions.assertTrue(response.content?.contains("hasMore")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}