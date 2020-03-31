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
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

class VacationControllerTest {

    @Test
    fun testUserVacation() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {

                createVacationParts(memberId, database)

                with(handleRequest(HttpMethod.Get, "api/hr/vacations/$memberId")) {
                    println(response.content)
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("\"days\": 10")!!)
                    Assertions.assertTrue(response.content?.contains("\"hours\": 5")!!)
                }
            }
        }
    }

    @Test
    fun testUsersVacation() {
        val memberId = UUID.randomUUID()
        val database = testDatabase
        withTestApplication({ testApplication(memberId, emptySet(), database) }) {
            rollbackTransaction(database) {

                createVacationParts(memberId, database)

                with(handleRequest(HttpMethod.Get, "api/hr/vacations")) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    Assertions.assertTrue(response.content?.contains("\"days\": 10")!!)
                    Assertions.assertTrue(response.content?.contains("\"hours\": 5")!!)
                }
            }
        }
    }

    private fun createVacationParts(memberId: UUID, database: Database) {
        val locationId = database.createLocation()
        //user earn 25 vacation days per year (2 per month)
        // +10d + 2*6d = 22d
        val userLocationId = database.createUserLocation(
            memberId,
            locationId,
            10,
            LocalDate.now().minusMonths(12).minusDays(5),
            LocalDate.now().minusMonths(6)
        )
        // +22d -5h
        database.createWorkingHour(memberId, LocalDate.now().minusMonths(12))
        // +22d -11h
        database.createWorkingHour(memberId, LocalDate.now().minusMonths(11), 6)
        // +22d - 5d -11h
        database.createAbsence(
            memberId,
            LocalDate.of(2020, 3, 2),
            LocalDate.of(2020, 3, 8)
        )
        // +17d - 6d -11h
        database.createAbsence(
            memberId,
            LocalDate.of(2020, 3, 9),
            LocalDate.of(2020, 3, 16)
        )
        // +9d +5h + 1 extra (25 % 12) = 10d 5h
    }
}