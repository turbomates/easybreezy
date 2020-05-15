package io.easybreezy.hr.api.controller

import io.easybreezy.hr.createCalendar
import io.easybreezy.hr.createHoliday
import io.easybreezy.hr.createLocation
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

class CalendarControllerTest {

    @Test
    fun `calendar import`() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, database) }) {
            rollbackTransaction(database) {
                val locationId = database.createLocation()
                val name = "Belarus custom calendar"

                with(handleRequest(HttpMethod.Post, "/api/hr/calendars") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "name" to name
                            "locationId" to locationId.toString()
                            "encodedCalendar" to "QkVHSU46VkNBTEVOREFSClZFUlNJT046Mi4wClBST0RJRDotLy9PZmZpY2UgSG9saWRheXMgTHRkLi8vRU4KQ0FMU0NBTEU6R1JFR09SSUFOCk1FVEhPRDpQVUJMSVNIClgtV1ItQ0FMTkFNRTpCZWxhcnVzIEhvbGlkYXlzClgtV1ItQ0FMREVTQzpQdWJsaWMgSG9saWRheXMgaW4gQmVsYXJ1cy4gUHJvdmlkZWQgYnkgaHR0cDovL3d3dy5vZmZpY2Vob2xpZGF5cy5jb20KUkVGUkVTSC1JTlRFUlZBTDtWQUxVRT1EVVJBVElPTjpQVDQ4SApYLVBVQkxJU0hFRC1UVEw6UFQ0OEgKWC1NUy1PTEstRk9SQ0VJTlNQRUNUT1JPUEVOOlRSVUUKQkVHSU46VkVWRU5UCkNMQVNTOlBVQkxJQwpVSUQ6MjAyMC0wMS0wMUJZNDE1cmVnY291bnRyeUB3d3cub2ZmaWNlaG9saWRheXMuY29tCkNSRUFURUQ6MjAyMDAxMjhUNTI4MThaCkRFU0NSSVBUSU9OOk5ldyBZZWFyJ3MgRGF5IGlzIGEgcHVibGljIGhvbGlkYXkgaW4gYWxsIGNvdW50cmllcyB0aGF0IG9ic2VydmUgdGhlIEdyZWdvcmlhbiBjYWxlbmRhciwgd2l0aCB0aGUgZXhjZXB0aW9uIG9mIElzcmFlbFxuXG5cblxuSW5mb3JtYXRpb24gcHJvdmlkZWQgYnkgd3d3Lm9mZmljZWhvbGlkYXlzLmNvbQpVUkw6aHR0cHM6Ly93d3cub2ZmaWNlaG9saWRheXMuY29tL2hvbGlkYXlzL2JlbGFydXMvaW50ZXJuYXRpb25hbC1uZXcteWVhcnMtZGF5CkRUU1RBUlQ7VkFMVUU9REFURToyMDIwMDEwMQpEVEVORDtWQUxVRT1EQVRFOjIwMjAwMTAyCkRUU1RBTVA6MjAyMDAxMDFUMDAwMDAwWgpMT0NBVElPTjpCZWxhcnVzClBSSU9SSVRZOjUKU0VRVUVOQ0U6MApTVU1NQVJZO0xBTkdVQUdFPWVuLXVzOkJlbGFydXM6IE5ldyBZZWFyJ3MgRGF5ClRSQU5TUDpPUEFRVUUKWC1NSUNST1NPRlQtQ0RPLUJVU1lTVEFUVVM6QlVTWQpYLU1JQ1JPU09GVC1DRE8tSU1QT1JUQU5DRToxClgtTUlDUk9TT0ZULURJU0FMTE9XLUNPVU5URVI6RkFMU0UKWC1NUy1PTEstQUxMT1dFWFRFUk5DSEVDSzpUUlVFClgtTVMtT0xLLUFVVE9GSUxMTE9DQVRJT046RkFMU0UKWC1NSUNST1NPRlQtQ0RPLUFMTERBWUVWRU5UOlRSVUUKWC1NSUNST1NPRlQtTVNOQ0FMRU5EQVItQUxMREFZRVZFTlQ6VFJVRQpYLU1TLU9MSy1DT05GVFlQRTowCkVORDpWRVZFTlQKRU5EOlZDQUxFTkRBUgo="
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/calendars")) {
                    Assertions.assertTrue(response.content?.contains(name)!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `calendar edit`() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, database) }) {
            rollbackTransaction(database) {
                val locationId = database.createLocation()
                val calendarId = database.createCalendar(locationId)
                val name = "New calendar name"

                with(handleRequest(HttpMethod.Post, "/api/hr/calendars/$calendarId") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "name" to name
                            "locationId" to locationId.toString()
                        }.toString()
                    )
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/calendars")) {
                    Assertions.assertTrue(response.content?.contains(name)!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun calendars() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, database) }) {
            rollbackTransaction(database) {
                val locationId = database.createLocation()
                database.createCalendar(locationId)

                with(handleRequest(HttpMethod.Get, "/api/hr/calendars")) {
                    Assertions.assertTrue(response.content?.contains("Belarus")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `calendar remove`() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, database) }) {
            rollbackTransaction(database) {
                val locationId = database.createLocation()
                val calendarId = database.createCalendar(locationId)
                val holidayId = database.createHoliday(calendarId)

                with(handleRequest(HttpMethod.Delete, "/api/hr/calendars/$calendarId") {
                    addHeader("Content-Type", "application/json")
                }) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                with(handleRequest(HttpMethod.Get, "/api/hr/calendars")) {
                    Assertions.assertFalse(response.content?.contains("Belarus")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
                // holidays must be deleted as cascade
                with(handleRequest(HttpMethod.Get, "/api/hr/calendars/holidays/$calendarId")) {
                    Assertions.assertFalse(response.content?.contains("New year")!!)
                    Assertions.assertFalse(response.content?.contains(holidayId.toString())!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `add holiday`() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, database) }) {
            rollbackTransaction(database) {
                val locationId = database.createLocation()
                val calendarId = database.createCalendar(locationId)
                val name = "Custom holiday"

                with(handleRequest(HttpMethod.Post, "/api/hr/calendars/holidays") {
                    addHeader("Content-Type", "application/json")
                    setBody(
                        json {
                            "calendarId" to calendarId.toString()
                            "day" to "2020-07-19"
                            "name" to name
                            "isWorkingDay" to true
                        }.toString()
                    )
                }) {
                    println(response.content)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }

                with(handleRequest(HttpMethod.Get, "/api/hr/calendars/holidays/$calendarId")) {
                    Assertions.assertTrue(response.content?.contains("2020-07-19")!!)
                    Assertions.assertTrue(response.content?.contains(name)!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun holidays() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, database) }) {
            rollbackTransaction(database) {
                val locationId = database.createLocation()
                val calendarId = database.createCalendar(locationId)
                database.createHoliday(calendarId)

                with(handleRequest(HttpMethod.Get, "/api/hr/calendars/holidays/$calendarId")) {
                    Assertions.assertTrue(response.content?.contains("New year")!!)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }
}
