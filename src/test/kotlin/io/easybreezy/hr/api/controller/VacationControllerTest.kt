package io.easybreezy.hr.api.controller

import io.easybreezy.hr.createAbsence
import io.easybreezy.hr.createLocation
import io.easybreezy.hr.createUserLocation
import io.easybreezy.hr.createWorkingHour
import io.easybreezy.rollbackTransaction
import io.easybreezy.testApplication
import io.easybreezy.testDatabase
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

class VacationControllerTest {

    @Test
    fun `user vacation`() {
        val memberId = UUID.randomUUID()
        withTestApplication({ testApplication() }) {
            rollbackTransaction {

                createVacationParts(memberId)

                with(handleRequest(HttpMethod.Get, "api/hr/vacations/$memberId")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("\"days\": 10")!!)
                    Assertions.assertTrue(response.content?.contains("\"hours\": 5")!!)
                }
            }
        }
    }

    @Test
    fun `user vacation with many locations`() {
        val memberId = UUID.randomUUID()
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                val locationId = testDatabase.createLocation()
                createVacationParts(memberId, locationId)

                // +13d
                val userLocationId = testDatabase.createUserLocation(
                    memberId,
                    locationId,
                    startedAt = LocalDate.now().minusMonths(24).minusDays(5),
                    endedAt = LocalDate.now().minusMonths(18)
                )

                with(handleRequest(HttpMethod.Get, "api/hr/vacations/$memberId")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("\"days\": 23")!!)
                    Assertions.assertTrue(response.content?.contains("\"hours\": 5")!!)
                }
            }
        }
    }

    @Test
    fun `users vacation`() {
        val memberId = UUID.randomUUID()
        withTestApplication({ testApplication() }) {
            rollbackTransaction {
                createVacationParts(memberId)

                with(handleRequest(HttpMethod.Get, "api/hr/vacations")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("\"days\": 10")!!)
                    Assertions.assertTrue(response.content?.contains("\"hours\": 5")!!)
                }
            }
        }
    }

    private fun createVacationParts(memberId: UUID, locationId: UUID = testDatabase.createLocation()) {
        // user earn 25 vacation days per year (2 per month)
        // +10d + 2*6d = 22d
        testDatabase.createUserLocation(
            memberId,
            locationId,
            10,
            LocalDate.of(2019, 3, 2),
            LocalDate.of(2019, 9, 10)
        )
        // +22d -5h
        testDatabase.createWorkingHour(memberId, LocalDate.of(2019, 3, 6))
        // +22d -11h
        testDatabase.createWorkingHour(memberId, LocalDate.of(2019, 3, 7), 6)
        // +22d - 5d -11h
        testDatabase.createAbsence(
            memberId,
            LocalDate.of(2019, 3, 4),
            LocalDate.of(2019, 3, 8)
        )
        // +17d - 6d -11h
        testDatabase.createAbsence(
            memberId,
            LocalDate.of(2019, 8, 19),
            LocalDate.of(2019, 8, 26)
        )
        // +9d +5h + 1 extra (25 % 12) = 10d 5h
    }
}
